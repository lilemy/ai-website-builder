declare namespace API {
  type AppAdminUpdateRequest = {
    id?: number
    appName?: string
    cover?: string
    priority?: number
  }

  type AppCreateRequest = {
    initPrompt?: string
  }

  type AppDeployRequest = {
    appId?: number
  }

  type AppQueryRequest = {
    pageNum?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    appName?: string
    initPrompt?: string
    codeGenType?: string
    priority?: number
    userId?: number
  }

  type AppUpdateRequest = {
    id?: number
    appName?: string
  }

  type AppVO = {
    id?: number
    appName?: string
    cover?: string
    initPrompt?: string
    codeGenType?: string
    deployKey?: string
    deployedTime?: string
    priority?: number
    userId?: number
    createTime?: string
    updateTime?: string
    user?: UserVO
  }

  type BaseResponseAppVO = {
    /** 响应状态码 */
    code?: number
    data?: AppVO
    /** 响应信息 */
    message?: string
  }

  type BaseResponseBoolean = {
    /** 响应状态码 */
    code?: number
    /** 响应数据 */
    data?: boolean
    /** 响应信息 */
    message?: string
  }

  type BaseResponseLoginUserVO = {
    /** 响应状态码 */
    code?: number
    data?: LoginUserVO
    /** 响应信息 */
    message?: string
  }

  type BaseResponseLong = {
    /** 响应状态码 */
    code?: number
    /** 响应数据 */
    data?: number
    /** 响应信息 */
    message?: string
  }

  type BaseResponsePageAppVO = {
    /** 响应状态码 */
    code?: number
    data?: PageAppVO
    /** 响应信息 */
    message?: string
  }

  type BaseResponsePageUserVO = {
    /** 响应状态码 */
    code?: number
    data?: PageUserVO
    /** 响应信息 */
    message?: string
  }

  type BaseResponseString = {
    /** 响应状态码 */
    code?: number
    /** 响应数据 */
    data?: string
    /** 响应信息 */
    message?: string
  }

  type BaseResponseUser = {
    /** 响应状态码 */
    code?: number
    data?: User
    /** 响应信息 */
    message?: string
  }

  type BaseResponseUserVO = {
    /** 响应状态码 */
    code?: number
    data?: UserVO
    /** 响应信息 */
    message?: string
  }

  type chatToGenCodeParams = {
    appId: number
    message: string
  }

  type DeleteRequest = {
    id?: number
  }

  type getAppVOByIdByAdminParams = {
    id: number
  }

  type getAppVOByIdParams = {
    id: number
  }

  type getAppVOByPageByAdminParams = {
    appQueryRequest: AppQueryRequest
  }

  type getChoicenessAppVOPageParams = {
    request: AppQueryRequest
  }

  type getLoginUserAppVOPageParams = {
    request: AppQueryRequest
  }

  type getUserByIdParams = {
    id: number
  }

  type getUserVOByIdParams = {
    id: number
  }

  type listUserVOByPageParams = {
    userQueryRequest: UserQueryRequest
  }

  type LoginUserVO = {
    id?: number
    userAccount?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
    createTime?: string
    updateTime?: string
  }

  type PageAppVO = {
    records?: AppVO[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type PageUserVO = {
    records?: UserVO[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type ServerSentEventString = true

  type serveStaticResourceParams = {
    deployKey: string
  }

  type User = {
    id?: number
    userAccount?: string
    userPassword?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
    editTime?: string
    createTime?: string
    updateTime?: string
    isDelete?: number
  }

  type UserCreateRequest = {
    userName?: string
    userAccount?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
  }

  type UserLoginRequest = {
    userAccount?: string
    userPassword?: string
  }

  type UserQueryRequest = {
    pageNum?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    id?: number
    userName?: string
    userAccount?: string
    userProfile?: string
    userRole?: string
  }

  type UserRegisterRequest = {
    userAccount?: string
    userPassword?: string
    checkPassword?: string
  }

  type UserUpdateRequest = {
    id?: number
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
  }

  type UserVO = {
    id?: number
    userAccount?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
    createTime?: string
  }
}
