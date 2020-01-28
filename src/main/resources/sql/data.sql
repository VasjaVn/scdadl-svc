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


INSERT INTO component_group(platform_id, name, created_date, created_by, updated_date, updated_by)
VALUES
    (1, 'cmp_grp_1', '2016-06-22 19:10:25-07', 'CREATED_NAME', NULL, NULL),
    (1, 'cmp_grp_2', '2016-06-22 19:10:25-07', 'CREATED_NAME', NULL, NULL),
    (1, 'enterprise', '2020-01-22 23:04:25-07', 'CRTD_BY_ME', NULL, NULL);


INSERT INTO component(component_group_id, name, asset_insight_id, created_date, created_by, updated_date, updated_by)
VALUES
	(1, 'cmpnt_1_1', 5567, '2016-06-22 19:10:25-07', 'CREATED_NAME_1_1', NULL, NULL),
    (1, 'cmpnt_1_2', 5682, '2016-06-22 19:10:25-07', 'CREATED_NAME_1_2', NULL, NULL),
    (2, 'cmpnt_2_1', 2111, '2016-06-22 19:10:25-07', 'CREATED_NAME_2_1', NULL, NULL),
    (2, 'cmpnt_2_2', 2112, '2016-06-22 19:10:25-07', 'CREATED_NAME_2_2', NULL, NULL),
    (3, 'java', 7777, '2020-01-22 23:04:25-07', 'CRTD_BY_ME', NULL, NULL ), -- component_id=5
    (3, 'js', 5555, '2020-01-22 23:04:25-07', 'CRTD_BY_ME', NULL, NULL ); -- component_id=6


INSERT INTO component_version(component_id, version, package_url, format, quality_grade, validated, version_avoid, validation_error, created_date, created_by, updated_date, updated_by)
VALUES
    (1, '1.1.1-1-1-1_1', 'http://package_url_1_1_1', 'ZIP', 'PRODUCTION', 'FALSE', 'DEPRECATED', 'Validated Error 1', '2016-06-22 19:10:25-07', '', NULL, NULL),
    (1, '1.1.1-1-1-1_2', 'http://package_url_1_1_2', 'ZIP', 'PRODUCTION', 'NEW', 'DEPRECATED', 'Validated Error 2', '2016-06-22 19:10:25-07', '', NULL, NULL),
    (2, '1.1.1-1-1-2_1', 'http://package_url_1_1_2', 'ZIP', 'PRODUCTION', 'TRUE', 'DEPRECATED', 'Validated Error 3', '2016-06-22 19:10:25-07', '', NULL, NULL),
    (2, '1.1.1-1-1-2_2', 'http://package_url_1_1_3', 'RAR', 'PRODUCTION', 'TRUE', 'DEPRECATED', 'Validated Error 4', '2016-06-22 19:10:25-07', '', NULL, NULL),
    (5, '1.7', 'http://package_url_java_1.7', 'ZIP', 'PRODUCTION', 'NEW', 'DEPRECATED', 'Validated Error 5', '2016-06-22 19:10:25-07', '', NULL, NULL),
    (5, '1.8', 'http://package_url_java_1.8', 'ZIP', 'PRODUCTION', 'TRUE', 'DEPRECATED', 'Validated Error 6', '2016-06-22 19:10:25-07', '', NULL, NULL),
    (6, '5.1', 'http://package_url_js_5.1', 'ZIP', 'PRODUCTION', 'NEW', 'DEPRECATED', 'Validated Error 7', '2016-06-22 19:10:25-07', '', NULL, NULL),
    (6, '7.3', 'http://package_url_js_7.3', 'ZIP', 'PRODUCTION', 'NEW', 'DEPRECATED', 'Validated Error 8', '2016-06-22 19:10:25-07', '', NULL, NULL);


INSERT INTO component_stack(name, next_auto_ver_start_at, created_date, created_by, updated_date, updated_by)
VALUES
    ('cmpnt_stack_1', '1.1.1', '2016-06-22 19:10:25-07', 'CRTD_BY_NM', NULL, NULL),
    ('cmpnt_stack_2', '1.2.2', '2016-06-22 19:10:25-07', 'CRTD_BY_NM', NULL, NULL),
    ('web', '2.3.4', '2016-06-22 19:10:25-07', 'CRTD_BY_NM', NULL, NULL); -- component_stack_id=3

INSERT INTO component_stack_version(component_stack_id, version, validated, created_date, created_by, updated_date, updated_by)
VALUES
    (3, '1.22.333.1', true, '2016-06-22 19:10:25-07', 'CRTD_BY_NM', NULL, NULL), -- component_stack_ver_id=1
    (3, '1.22.333.2', true, '2016-06-22 19:10:25-07', 'CRTD_BY_NM', NULL, NULL), -- component_stack_ver_id=2
    (3, '1.22.333.3', true, '2016-06-22 19:10:25-07', 'CRTD_BY_NM', NULL, NULL); -- component_stack_ver_id=3

INSERT INTO cmpntstackver_cmpntver(component_stack_ver_id, component_ver_id, trigger_new_stack_ver, min_grade)
VALUES
    (1, 5, true, 'PRODUCTION'),
    --(1, 7, true, 'PRODUCTION'),
    --(2, 5, true, 'PRODUCTION'),
    (2, 6, true, 'PRODUCTION'),
    --(3, 6, true, 'PRODUCTION'),
    (3, 8, true, 'PRODUCTION');