import LandingPage from "./components/landingPage/LandingPage";
import MoreInformation from "./components/landingPage/MoreInformation";
import PageNotFound from "./components/PageNotFound";

export const routes = [
    { path: "", name: 'LandingPage', component: LandingPage },
    { path: "/home", component: LandingPage },
    {
        name: "moreInformation",
        path: "/MoreInformation",
        component: MoreInformation,
        props: true,
        beforeEnter: (to, from, next) => {
            // alert(to.query.id);
            // if(to.path=='/MoreInformation' && (String(to.query.id)!=="" || String(to.query.id)!=="undefined")){
            //     next({name:'LandingPage'});
            // }

            if (to.query.id !== "") {
                next();
            }
            

            else {
                next({name:'LandingPage'});
            }            
        }
    },
    { path: "*", component: PageNotFound },
];
