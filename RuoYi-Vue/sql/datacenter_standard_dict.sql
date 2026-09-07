-- =============================================================
-- 行业标准管理 - 字典数据与权限菜单初始化脚本
-- 说明：
--   1. 新增两个字典类型 data_standard_type（标准类型）、data_extract_type（提取类型）
--   2. 补充 datacenter:standard:import 权限菜单（前端列表的「导入」按钮依赖该权限点）
-- 脚本可重复执行（幂等）：
--   - 字典类型用 INSERT ... ON DUPLICATE KEY UPDATE
--   - 字典数据先按 dict_type 清理再插入，保证不重复
--   - 菜单权限按 perms 唯一性判断，已存在则不插入
-- 适用库：ry-vue
-- =============================================================

-- -------------------------------------------------------------
-- 1. 字典类型
-- -------------------------------------------------------------
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('标准类型', 'data_standard_type', '0', 'admin', sysdate(), '行业标准类型：1=行业标准、2=自定义标准')
ON DUPLICATE KEY UPDATE
    dict_name = '标准类型',
    remark    = '行业标准类型：1=行业标准、2=自定义标准';

INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('提取类型', 'data_extract_type', '0', 'admin', sysdate(), '标准提取类型：1=file、2=建模数据源')
ON DUPLICATE KEY UPDATE
    dict_name = '提取类型',
    remark    = '标准提取类型：1=file、2=建模数据源';

-- -------------------------------------------------------------
-- 2. 字典数据（先按 dict_type 清理，避免重复）
-- -------------------------------------------------------------
DELETE FROM sys_dict_data WHERE dict_type IN ('data_standard_type', 'data_extract_type');

-- 2.1 标准类型字典项
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, '行业标准', '1', 'data_standard_type', '', 'primary', 'Y', '0', 'admin', sysdate(), '行业公开标准'),
(2, '自定义标准', '2', 'data_standard_type', '', 'success', 'N', '0', 'admin', sysdate(), '业务自定义标准');

-- 2.2 提取类型字典项
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, 'file', '1', 'data_extract_type', '', 'info', 'Y', '0', 'admin', sysdate(), '基于文件的提取'),
(2, '建模数据源', '2', 'data_extract_type', '', 'warning', 'N', '0', 'admin', sysdate(), '基于建模数据源的提取');

-- -------------------------------------------------------------
-- 3. 权限菜单：datacenter:standard:import
-- 前端列表「导入」按钮依赖该权限点，若菜单已存在则不重复插入
-- parent_id 取 datacenter:standard 已存在目录/菜单的父级，需根据实际父菜单 ID 调整
-- 此处使用动态查询：自动定位到标准管理目录的父 ID
-- -------------------------------------------------------------
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '标准导入', IFNULL((SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE perms = 'datacenter:standard:add' LIMIT 1) t), 0),
       7, '#', '', '', 1, 0, 'F', '0', '0', 'datacenter:standard:import', '#', 'admin', sysdate(), '行业标准导入按钮'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'datacenter:standard:import');

-- -------------------------------------------------------------
-- 4. 角色权限授权（可选）
-- 若希望超管（role_id=1）默认拥有导入权限，下列语句确保 sys_role_menu 关联存在
-- -------------------------------------------------------------
-- INSERT INTO sys_role_menu (role_id, menu_id)
-- SELECT 1, menu_id FROM sys_menu WHERE perms = 'datacenter:standard:import'
-- WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu rm JOIN sys_menu m ON rm.menu_id = m.menu_id WHERE rm.role_id = 1 AND m.perms = 'datacenter:standard:import');

-- -------------------------------------------------------------
-- 执行完成后，登录超管账号进入 系统管理 -> 字典管理 可看到新增的两个字典
-- 进入 角色管理 -> 菜单权限 勾选「标准导入」即可显示前端导入按钮
-- -------------------------------------------------------------
