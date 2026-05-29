export default [  {
    path: '/tool/gen-edit',
    name: 'gen',
    children: [
        {
            path: 'index/:tableId(\\d+)',
            component: () => import('@/views/sys/gen/editTable'),
            name: 'GenEdit',
            meta: { title: '修改生成配置', activeMenu: '/tool/gen' }
        }
    ]
}
]