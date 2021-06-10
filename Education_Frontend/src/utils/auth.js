import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

const UserTypeKey = 'user-type'

export function getUserType() {
  return Cookies.get(UserTypeKey)
}

export function setUserType(token) {
  return Cookies.set(UserTypeKey, token)
}

const RememberKey = 'isRemember'

export function getRemember() {
  return !!Cookies.get(RememberKey)
}

export function setRemember() {
  return Cookies.set(RememberKey, 'sth')
}

export function removeRemember() {
  return Cookies.remove(RememberKey)
}

const CredentialKey = 'credential'

export function getCredential() {
  const val = Cookies.get(CredentialKey)
  if (!val) return undefined
  return {
    number: val.substr(0, 11),
    password: val.substr(11)
  }
}

export function setCredential(number, password) {
  // TODO: timeout
  return Cookies.set(CredentialKey, number + password)
}

export function removeCredential(number, password) {
  return Cookies.remove(CredentialKey)
}
