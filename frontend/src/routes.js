import LandingPage from "./components/landingPage/LandingPage";
import MoreInformation from "./components/landingPage/MoreInformation";

export const routes = [
    { path: "", component: LandingPage },
    { path: "/home", component: LandingPage },
    { path: "/moreInformation", component: MoreInformation  }
];