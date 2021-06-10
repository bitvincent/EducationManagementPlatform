const getters = {
  sidebar: state => state.app.sidebar,
  size: state => state.app.size,
  device: state => state.app.device,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  introduction: state => state.user.introduction,
  roles: state => state.user.roles,
  permission_routes: state => state.permission.routes,
  errorLogs: state => state.errorLog.logs,
  isTeacher: state => state.user.userType === 'teacher',
  isStudent: state => state.user.userType === 'student',
  loggedIn: state => !!state.user.token,
  currentHomework: state => state.homework.currentHomework,
  teacherId: state => state.user.teacherId,
  classCategories: state => state.homepage.classCategories
}
export default getters
