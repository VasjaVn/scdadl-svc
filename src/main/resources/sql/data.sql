INSERT INTO platform(name, created_date, created_by, updated_date, updated_by)
VALUES
    ('compas', '2016-06-22 19:10:25-07', 'CREATED_NAME', NULL, NULL),
    ('content', '2016-06-22 19:10:25-07', 'CREATED_NAME', NULL, NULL),
    ('eikon', '2016-06-22 19:10:25-07', 'CREATED_NAME', NULL, NULL),
    ('elektron', '2016-06-22 19:10:25-07', 'CREATED_NAME', NULL, NULL),
    ('risk', '2016-06-22 19:10:25-07', 'CREATED_NAME', NULL, NULL),
    ('transactions', '2016-06-22 19:10:25-07', 'CREATED_NAME', NULL, NULL),
    ('wealth management', '2016-06-22 19:10:25-07', 'CREATED_NAME', NULL, NULL),
    ('f&r other', '2016-06-22 19:10:25-07', 'CREATED_NAME', NULL, NULL),
    ('ebs', '2016-06-22 19:10:25-07', 'CREATED_NAME', NULL, NULL),
    ('legal', '2016-06-22 19:10:25-07', 'CREATED_NAME', NULL, NULL),
    ('pgo', '2016-06-22 19:10:25-07', 'CREATED_NAME', NULL, NULL),
    ('trta_onesource', '2016-06-22 19:10:25-07', 'CREATED_NAME', NULL, NULL),
    ('tr other', '2016-06-22 19:10:25-07', 'CREATED_NAME', NULL, NULL);


INSERT INTO component_group(platform_fk, name, created_date, created_by, updated_date, updated_by)
VALUES
    (1, 'cmp_grp_1', '2016-06-22 19:10:25-07', 'CREATED_NAME', NULL, NULL),
    (1, 'cmp_grp_2', '2016-06-22 19:10:25-07', 'CREATED_NAME', NULL, NULL);


INSERT INTO component(component_group_fk, name, asset_insight_id, created_date, created_by, updated_date, updated_by)
VALUES
	(1, 'cmpnt_1_1', 5567, '2016-06-22 19:10:25-07', 'CREATED_NAME_1_1', NULL, NULL),
    (1, 'cmpnt_1_2', 5682, '2016-06-22 19:10:25-07', 'CREATED_NAME_1_2', NULL, NULL),
    (2, 'cmpnt_2_1', 2111, '2016-06-22 19:10:25-07', 'CREATED_NAME_2_1', NULL, NULL),
    (2, 'cmpnt_2_2', 2112, '2016-06-22 19:10:25-07', 'CREATED_NAME_2_2', NULL, NULL);


INSERT INTO component_version(component_fk, version, package_url, format, quality_grade, validated, version_avoid, validation_error, created_date, created_by, updated_date, updated_by)
VALUES
    (1, '1.1.1-1-1-1_1', 'http://package_url_1_1_1', 'ZIP', 'PRODUCTION', 'NEW', 'DEPRECATED', 'Validated_Error', '2016-06-22 19:10:25-07', '', NULL, NULL),
    (1, '1.1.1-1-1-1_2', 'http://package_url_1_1_2', 'ZIP', 'DEVELOPMENT', 'TRUE', 'DEPRECATED', 'Validated_Error', '2016-06-22 19:10:25-07', '', NULL, NULL),
    (2, '1.1.1-1-1-2_1', 'http://package_url_1_1_2', 'ZIP', 'LAB', 'FALSE', 'DEPRECATED', 'Validated_Error', '2016-06-22 19:10:25-07', '', NULL, NULL),
    (2, '1.1.1-1-1-2_2', 'http://package_url_1_1_3', 'RAR', 'PRE_PRODUCTION', 'PROCESSING', 'DEPRECATED', 'Validated_Error', '2016-06-22 19:10:25-07', '', NULL, NULL);

