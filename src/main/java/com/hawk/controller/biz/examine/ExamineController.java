package com.hawk.controller.biz.examine;

import java.util.List;
import java.util.Arrays;

import cn.hutool.core.bean.BeanUtil;
import com.hawk.biz.entity.Examine;
import com.hawk.biz.service.ExamineService;
import com.hawk.controller.biz.examine.form.ExamineForm;
import com.hawk.framework.annotation.common.Log;
import com.hawk.framework.annotation.common.RepeatSubmit;
import com.hawk.framework.base.BaseController;
import com.hawk.framework.enums.BusinessType;
import com.hawk.framework.validate.AddGroup;
import com.hawk.framework.validate.EditGroup;
import com.hawk.framework.web.page.PageInfo;
import com.hawk.framework.web.resp.R;
import com.hawk.utils.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

/**
 * 【请填写功能名称】
 *
 * @author hawk
 * @date 2025-02-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/biz/examine")
public class ExamineController extends BaseController {

    private final ExamineService examineService;

    /**
     * 查询【请填写功能名称】列表
     */
    //@SaCheckPermission("biz:examine:list")
    @GetMapping("/list")
    public R<PageInfo<Examine>> list(ExamineForm form) {
        Examine paramBean = BeanUtil.copyProperties(form, Examine.class);
        PageInfo<Examine> pageInfo = examineService.selectPageList(paramBean, form.getPageSize(), form.getPageNum());
        return R.ok(pageInfo);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    //@SaCheckPermission("biz:examine:export")
    @PostMapping("/export")
    public void export(ExamineForm form, HttpServletResponse response) {
        Examine paramBean = BeanUtil.copyProperties(form, Examine.class);
        List<Examine> list = examineService.listByConditions(paramBean);
        ExcelUtil.exportExcel(list, "【请填写功能名称】", Examine.class, response);
    }

    /**
     * 获取【请填写功能名称】详细信息
     *
     * @param exameeId 主键
     */
    //@SaCheckPermission("biz:examine:query")
    @GetMapping("/{exameeId}")
    public R<Examine> getInfo(@NotNull(message = "主键不能为空")
                              @PathVariable Long exameeId) {
        return R.ok(examineService.getByPrimaryKey(exameeId));
    }

    /**
     * 新增【请填写功能名称】
     */
    //@SaCheckPermission("biz:examine:add")
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ExamineForm form) {
        Examine paramBean = BeanUtil.copyProperties(form, Examine.class);
        examineService.insert(paramBean);
        return R.ok();
    }

    /**
     * 修改【请填写功能名称】
     */
    //@SaCheckPermission("biz:examine:edit")
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ExamineForm form) {
        Examine paramBean = BeanUtil.copyProperties(form, Examine.class);
        examineService.updateByPrimaryKeySelective(paramBean);
        return R.ok();
    }

    /**
     * 删除【请填写功能名称】
     *
     * @param examineIds 主键串
     */
    //@SaCheckPermission("biz:examine:remove")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{exameeIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] examineIds) {
        examineService.deleteBatchByPrimaryKeys(Arrays.asList(examineIds));
        return R.ok();
    }
}
