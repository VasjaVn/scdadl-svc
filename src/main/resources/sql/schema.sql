DROP TABLE IF EXISTS component;
CREATE TABLE component(
        id serial PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        created_date TIMESTAMP NOT NULL,
        created_by VARCHAR(255) NOT NULL,
        updated_date TIMESTAMP,
        updated_by VARCHAR(255)
    );

DROP TABLE IF EXISTS component_version;
CREATE TABLE component_version(
        id serial PRIMARY KEY,
        component_fk INTEGER REFERENCES component(id),
        version VARCHAR(255) NOT NULL,
        package_url VARCHAR(255) NOT NULL,
        format VARCHAR(255) NOT NULL,
        quality_grade VARCHAR(255) NOT NULL,
        validated BOOLEAN NOT NULL,
        version_avoid VARCHAR(255),
        created_date TIMESTAMP NOT NULL,
        created_by VARCHAR(255) NOT NULL,
        updated_date TIMESTAMP,
        updated_by VARCHAR(255)
    );