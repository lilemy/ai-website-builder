// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 管理员删除应用 POST /app/admin/delete */
export async function deleteAppByAdmin(body: API.DeleteRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/app/admin/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 管理员根据 id 获取应用详情 GET /app/admin/get/vo */
export async function getAppVoByIdByAdmin(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getAppVOByIdByAdminParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseAppVO>('/app/admin/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 管理员分页获取应用列表 GET /app/admin/list/vo */
export async function getAppVoByPageByAdmin(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getAppVOByPageByAdminParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageAppVO>('/app/admin/list/vo', {
    method: 'GET',
    params: {
      ...params,
      appQueryRequest: undefined,
      ...params['appQueryRequest'],
    },
    ...(options || {}),
  })
}

/** 管理员更新应用 POST /app/admin/update */
export async function updateAppByAdmin(
  body: API.AppAdminUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>('/app/admin/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 应用聊天生成代码（流式 SSE） GET /app/chat/gen/code */
export async function chatToGenCode(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.chatToGenCodeParams,
  options?: { [key: string]: any }
) {
  return request<API.ServerSentEventString[]>('/app/chat/gen/code', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 获取精选应用列表 GET /app/choiceness/list/vo */
export async function getChoicenessAppVoPage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getChoicenessAppVOPageParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageAppVO>('/app/choiceness/list/vo', {
    method: 'GET',
    params: {
      ...params,
      request: undefined,
      ...params['request'],
    },
    ...(options || {}),
  })
}

/** 创建应用 POST /app/create */
export async function createApp(body: API.AppCreateRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/app/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 删除应用 DELETE /app/delete */
export async function deleteApp(body: API.DeleteRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/app/delete', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 应用部署 POST /app/deploy */
export async function deployApp(body: API.AppDeployRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseString>('/app/deploy', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 根据 id 获取应用详情 GET /app/get/vo */
export async function getAppVoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getAppVOByIdParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseAppVO>('/app/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 获取登录用户应用列表 GET /app/my/list/vo */
export async function getLoginUserAppVoPage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getLoginUserAppVOPageParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageAppVO>('/app/my/list/vo', {
    method: 'GET',
    params: {
      ...params,
      request: undefined,
      ...params['request'],
    },
    ...(options || {}),
  })
}

/** 更新应用 PUT /app/update */
export async function updateApp(body: API.AppUpdateRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/app/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
