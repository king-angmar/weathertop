package xyz.wongs.weathertop.war3.web.controller.system;


import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.wongs.weathertop.war3.common.annotation.WebLog;
import xyz.wongs.weathertop.war3.common.core.page.TableDataInfo;
import xyz.wongs.weathertop.war3.common.domain.AjaxResult;
import xyz.wongs.weathertop.war3.common.enums.BusinessType;
import xyz.wongs.weathertop.war3.common.utils.ShiroUtils;
import xyz.wongs.weathertop.war3.common.utils.poi.ExcelUtil;
import xyz.wongs.weathertop.war3.system.entity.SysDictData;
import xyz.wongs.weathertop.war3.system.service.SysDictDataService;
import xyz.wongs.weathertop.war3.web.controller.base.AbsController;

import java.util.List;

/**
 * 数据字典信息
 *
 * @author ruoyi
 */
@Slf4j
@Controller
@RequestMapping("/system/dict/data")
public class SysDictDataController extends AbsController {
    private String prefix = "system/dict/data";

    @Autowired
    private SysDictDataService sysDictDataService;

    @RequiresPermissions("system:dict:view")
    @GetMapping()
    public String dictData() {
        return prefix + "/data";
    }

    @PostMapping("/list")
    @RequiresPermissions("system:dict:list")
    @ResponseBody
    public TableDataInfo list(SysDictData dictData) {
        startPage();
        List<SysDictData> list = sysDictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    @WebLog(title = "字典数据", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:dict:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysDictData dictData) {
        List<SysDictData> list = sysDictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        return util.exportExcel(list, "字典数据");
    }

    /**
     * 新增字典类型
     */
    @GetMapping("/add/{dictType}")
    public String add(@PathVariable("dictType") String dictType, ModelMap mmap) {
        mmap.put("dictType", dictType);
        return prefix + "/add";
    }

    /**
     * 新增保存字典类型
     */
    @WebLog(title = "字典数据", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:dict:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysDictData dict) {
        dict.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(Integer.parseInt(sysDictDataService.insertDictData(dict).toString()));
    }

    /**
     * 修改字典类型
     */
    @GetMapping("/edit/{dictCode}")
    public String edit(@PathVariable("dictCode") Long dictCode, ModelMap mmap) {
        mmap.put("dict", sysDictDataService.selectByPrimaryKey(dictCode));
        return prefix + "/edit";
    }

    /**
     * 修改保存字典类型
     */
    @WebLog(title = "字典数据", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:dict:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysDictData dict) {
        dict.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(sysDictDataService.updateByPrimaryKey(dict));
    }

    @WebLog(title = "字典数据", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:dict:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sysDictDataService.deleteDictDataByIds(ids));
    }
}
