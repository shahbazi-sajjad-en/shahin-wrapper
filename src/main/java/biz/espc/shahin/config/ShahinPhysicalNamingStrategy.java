package biz.espc.shahin.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Objects;

/**
 * provided by ESPC software team
 * created on 1/28/2026 at 10:49 AM
 */
public class ShahinPhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    private static final String PREFIX ="SHAHIN_";
    private static final String SUFFIX = "_";

    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return super.toPhysicalTableName(this.toSnakeCase(name, PREFIX, ""), context);
    }

    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
        return super.toPhysicalSequenceName(this.toSnakeCase(name, PREFIX, ""), context);
    }

    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return super.toPhysicalColumnName(this.toSnakeCase(name, "", SUFFIX), context);
    }

    private Identifier toSnakeCase(Identifier id, String prefix, String suffix) {
        if (Objects.isNull(id)) {
            return null;
        } else {
            String name = id.getText();
            String snakeName = prefix.concat(name.
                    replaceAll("([a-z]+)([A-Z]+)", "$1\\_$2")
                    .toLowerCase()
                    .concat(suffix));
            return !snakeName.equals(name) ? new Identifier(snakeName, id.isQuoted()) : id;
        }
    }
}