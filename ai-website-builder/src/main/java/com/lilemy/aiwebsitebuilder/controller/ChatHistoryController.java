package com.lilemy.aiwebsitebuilder.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.lilemy.aiwebsitebuilder.common.BaseResponse;
import com.lilemy.aiwebsitebuilder.common.ResultCode;
import com.lilemy.aiwebsitebuilder.common.ResultUtils;
import com.lilemy.aiwebsitebuilder.constant.UserConstant;
import com.lilemy.aiwebsitebuilder.exception.ThrowUtils;
import com.lilemy.aiwebsitebuilder.model.dto.chathistory.ChatHistoryQueryRequest;
import com.lilemy.aiwebsitebuilder.model.entity.ChatHistory;
import com.lilemy.aiwebsitebuilder.model.vo.chathistory.ChatHistoryVO;
import com.lilemy.aiwebsitebuilder.service.ChatHistoryService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 对话历史 控制层。
 *
 * @author lilemy
 * @since 2026-04-01 15:07
 */
@RestController
@RequestMapping("/chatHistory")
public class ChatHistoryController {

    @Resource
    private ChatHistoryService chatHistoryService;

    @Operation(summary = "分页获取单个应用的对话历史（游标查询）")
    @GetMapping("/app/{appId}")
    public BaseResponse<Page<ChatHistoryVO>> getChatHistoryVOPage(@PathVariable Long appId,
                                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                                  @RequestParam(required = false) LocalDateTime lastCreateTime) {
        Page<ChatHistoryVO> result = chatHistoryService.getChatHistoryVOPage(appId, pageSize, lastCreateTime);
        return ResultUtils.success(result);
    }

    @Operation(summary = "管理员分页查询所有对话历史")
    @GetMapping("/admin/list/vo")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<ChatHistory>> getAllChatHistoryPageByAdmin(ChatHistoryQueryRequest request) {
        ThrowUtils.throwIf(request == null, ResultCode.PARAMS_ERROR);
        int pageNum = request.getPageNum();
        int pageSize = request.getPageSize();
        // 查询数据
        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(request);
        Page<ChatHistory> result = chatHistoryService.page(Page.of(pageNum, pageSize), queryWrapper);
        return ResultUtils.success(result);
    }
}
