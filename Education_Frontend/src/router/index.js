import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

/* Layout */
import Layout from '@/layout';

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/homepage',
    children: [
      {
        path: '/homepage',
        name: 'Homepage',
        component: () => import('@/views/homepage')
      },
      {
        path: '/my-certificate',
        name: 'MyCertificate',
        component: () => import('@/views/certificate/my-certificate')
      },
      {
        path: '/certificate-detail/:certificateId',
        component: () => import('@/views/certificate/certificate-detail')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login')
  },
  {
    path: '/class',
    component: Layout,
    children: [
      {
        path: '',
        name: 'MyClassList',
        component: () => import('@/views/class/list')
      },
      {
        path: '/class/:id',
        name: 'ClassDetail',
        props: true,
        component: () => import('@/views/class/detail')
      },
      {
        path: '/class/:id/publish-certificate',
        name: 'PublishCertificate',
        props: true,
        component: () => import('@/views/certificate/publish-certificate')
      },
      {
        path: '/class/:id/learn-process',
        name: 'LearnProcess',
        props: true,
        component: () => import('@/views/class/learn-process')
      },
      {
        path: '/class/:id/homework',
        name: 'Homework',
        props: true,
        component: () => import('@/views/class/homework/index')
      },
      {
        path: '/class/:id/homework/publish/:homeworknumber',
        name: 'HomeworkPublish',
        props: route => ({
          id: route.params.id,
          homeworknumber: parseInt(route.params.homeworknumber)
        }),
        component: () => import('@/views/class/homework/publish')
      },
      {
        path: '/class/:id/homework/publish_detail/:hwNum',
        name: 'HomeworkPublishDetail',
        props: route => ({
          id: route.params.id,
          hwNum: parseInt(route.params.hwNum)
        }),
        component: () => import('@/views/class/homework/publishDetail')
      },
      {
        path: '/class/:id/homework/correct',
        name: 'HomeworkCorrect',
        props: true,
        component: () => import('@/views/class/homework/correct')
      },
      {
        path: '/class/:id/homework/corrected',
        name: 'HomeworkCorrected',
        props: true,
        component: () => import('@/views/class/homework/detail')
      },
      {
        path: '/class/:id/homework/submit',
        name: 'HomeworkSubmit',
        props: true,
        component: () => import('@/views/class/homework/submit')
      },
      {
        path: '/class/:id/homework/not_marked',
        name: 'HomeworkNotMarked',
        props: true,
        component: () => import('@/views/class/homework/detail')
      },
      {
        path: '/class/:id/homework/marked',
        name: 'HomeworkMarked',
        props: true,
        component: () => import('@/views/class/homework/detail')
      },
      {
        path: '/publish',
        name: 'PublishClass',
        component: () => import('@/views/class/publish')
      }
    ]
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
];

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [];

const createRouter = () =>
  new Router({
    mode: 'history', // require service support
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes
  });

const router = createRouter();

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher; // reset router
}

export default router;
