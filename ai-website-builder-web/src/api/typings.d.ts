declare namespace API {
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

  type BaseResponsePageUserVO = {
    /** 响应状态码 */
    code?: number
    data?: PageUserVO
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

  type DeleteRequest = {
    id?: number
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

  type PageUserVO = {
    records?: UserVO[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
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
