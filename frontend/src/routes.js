import LandingPage from "./components/landingPage/LandingPage";
import MoreInformation from "./components/landingPage/MoreInformation";
import PageNotFound from "./components/PageNotFound";

export const routes = [
    { path: "", component: LandingPage },
    { path: "/home", component: LandingPage },
    {   name: "moreInformation",
        path: "/MoreInformation",
        component: MoreInformation,
        props: true,
        beforeEnter: (to, from, next) => {
            if(to.query.id != "") {
                next();
            } else {
                next({name: 'LandingPage'});
            }
        }
    },
    { path: "*", component: PageNotFound },
];


