package com.borio.authorization.config.multinenancy.client.per.database.services;

import com.borio.authorization.config.multinenancy.client.per.database.exceptions.TenantCreationException;
import com.borio.authorization.config.multinenancy.client.per.database.repositories.TenantRepository;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
@EnableConfigurationProperties(LiquibaseProperties.class)
public class TenantManagementServiceImpl implements TenantManagementService  {

    private final EncryptionService encryptionService;
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final LiquibaseProperties liquibaseProperties;
    private final ResourceLoader resourceLoader;
    private final TenantRepository tenantRepository;

    private final String urlPrefix;
    private final String secret;
    private final String salt;

    private static final String VALID_DATABASE_NAME_REGEXP = "[A-Za-z0-9_]*";

    @Autowired
    public TenantManagementServiceImpl(EncryptionService encryptionService,
                                       DataSource dataSource,
                                       JdbcTemplate jdbcTemplate,
                                       @Qualifier("tenantLiquibaseProperties")
                                               LiquibaseProperties liquibaseProperties,
                                       ResourceLoader resourceLoader,
                                       TenantRepository tenantRepository,
                                       @Value("${multitenancy.tenant.datasource.url-prefix}")
                                               String urlPrefix,
                                       @Value("${encryption.secret}")
                                               String secret,
                                       @Value("${encryption.salt}")
                                               String salt
    ) {
        this.encryptionService = encryptionService;
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        this.liquibaseProperties = liquibaseProperties;
        this.resourceLoader = resourceLoader;
        this.tenantRepository = tenantRepository;
        this.urlPrefix = urlPrefix;
        this.secret = secret;
        this.salt = salt;
    }


    @Override
    public void createTenant(String tenantId, String db, String password) {

        if (!db.matches(VALID_DATABASE_NAME_REGEXP)) {
            throw new TenantCreationException("Invalid db name: " + db);
        }

        String url = urlPrefix+db;
        String encryptedPassword = encryptionService.encrypt(password, secret, salt);
        try {
            createDatabase(db, encryptedPassword);
        } catch (DataAccessException e) {
            throw new TenantCreationException("Error when creating db: " + db, e);
        }

        try (Connection connection = DriverManager.getConnection(url, db, password)) {
            DataSource tenantDataSource = new SingleConnectionDataSource(connection, false);
            runLiquibase(tenantDataSource);
        } catch (SQLException | LiquibaseException e) {
            throw new TenantCreationException("Error when populating db: ", e);
        }

    }

    private void runLiquibase(DataSource tenantDataSource) throws LiquibaseException {
        SpringLiquibase liquibase = getSpringLiquibase(dataSource);
        liquibase.afterPropertiesSet();
    }

    private SpringLiquibase getSpringLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setResourceLoader(resourceLoader);
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(liquibaseProperties.getChangeLog());
        liquibase.setContexts(liquibaseProperties.getContexts());
        return liquibase;
    }

    private void createDatabase(String db, String password) {
        jdbcTemplate.execute((StatementCallback<Boolean>) stmt ->
                stmt.execute("CREATE DATABASE " + db));
        jdbcTemplate.execute((StatementCallback<Boolean>) stmt ->
                stmt.execute("CREATE USER " + db + " WITH ENCRYPTED PASSWORD '" + password + "'"));
        jdbcTemplate.execute((StatementCallback<Boolean>) stmt ->
                stmt.execute("GRANT ALL PRIVILEGES ON DATABASE " + db + " TO " + db));
    }
}
