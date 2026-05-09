module codes.lab {
    requires java.desktop;

    requires java.sql;
    requires mysql.connector.j;
    exports codes.lab9.UI;
    exports codes.lab9.Objects;
    exports codes.lab9.Validations;
}