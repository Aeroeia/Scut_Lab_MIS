import Cookies from 'js-cookie'

const TokenKey = 'token'
const RoleKey = 'role'
const RealIdKey = 'realId'
const RoleNameKey = 'roleName'
const UserNameKey = 'userName'

// Token相关操作
export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  Cookies.remove(TokenKey)
  Cookies.remove(RoleKey)
  Cookies.remove(RealIdKey)
  Cookies.remove(RoleNameKey)
}

// 角色相关操作
export function getRole() {
  return Cookies.get(RoleKey)
}

export function setRole(role) {
  return Cookies.set(RoleKey, role)
}

export function getRealId() {
  return Cookies.get(RealIdKey)
}

export function setRealId(realId) {
  return Cookies.set(RealIdKey, realId)
}

export function getRoleName() {
  return Cookies.get(RoleNameKey)
}

export function setRoleName(roleName) {
  return Cookies.set(RoleNameKey, roleName)
}
export function setUserName(userName){
  return Cookies.set(UserNameKey,userName)
}
export function getUserName(){
  return Cookies.get(UserNameKey)
}
// 检查是否为管理员
export function isAdmin() {
  return getRole() === '1'
}

// 检查是否为教师
export function isTeacher() {
  return getRole() === '2'
}

// 检查是否为学生
export function isStudent() {
  return getRole() === '3'
}

// 路由权限检查
export function hasPermission(route, role) {
  if (route.meta && route.meta.roles) {
    return route.meta.roles.includes(role)
  } else {
    return true
  }
} 