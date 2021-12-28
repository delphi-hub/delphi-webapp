import { createRouter, createWebHistory } from "vue-router";

import * as pages from "@/pages"

const routerHistory = createWebHistory(process.env.BASE_URL);

const router = createRouter({
    history: routerHistory,
    routes: [
        { path: '/', component: pages.Home, name: "home" },
        { path: '/maven/:groupId', component: pages.MavenGroupView, name: "maven-g" },
        { path: '/maven/:groupId/:artifactId', component: pages.MavenArtifactView, name: "maven-ga" },
        { path: '/maven/:groupId/:artifactId/:version', component: pages.MavenArtifactVersionView, name: "maven-gav" }
    ]
})

export default router;