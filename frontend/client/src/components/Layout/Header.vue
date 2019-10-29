<template>
    <div class="component">
        <!-- This part should be used in every page - Start -->
    <div class="container-fluid sticky-top shadow">
        <div class="row align-items-center navigation">
            <div class="col-sm-2"> 
                <img src="D:\Delphi project\delphi-webapp\frontend\client\src\assets\Logo light gray text outline@4x.png" class="float-left" width="160px" >
            </div>
            <div class="col-sm-6">
            </div>
            <div class="col-sm-4">
                    <nav class="navbar navbar-expand-sm navbar-dark justify-content-end">
                        <ul class="navbar-nav">
                            <li class="nav-item active">
                                <a class="nav-link" href="..\frontend\client\src\components\landingPage\LandingPage.vue"><h5 class="nav-text">Home</h5></a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="#"><h5 class="nav-text">Statistics</h5></a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="#"><h5 class="nav-text">News</h5></a>
                            </li>
                            <li class="nav-item">
                                    <a class="nav-link" href="#"><h5 class="nav-text">About</h5></a>
                            </li>
                            <li class="nav-item">
                                    <a class="nav-link" href="#"><h5 class="nav-text">Tutorials</h5></a>
                            </li>
                        </ul>     
                    </nav>
            </div>
        </div>
    </div>
    <!-- This part should be used in every page - End -->
    <br>
    <br>
    <!-- This part should be used in every page - Start -->
    <footer class="fixed-bottom shadow foot">
        Delphi is maintained by the
        <a href="https://www.hni.uni-paderborn.de/en/software-engineering/" class="footerLinks">Secure Software Engineering group</a>
        at
        <a href="https://www.uni-paderborn.de/" class="footerLinks">Paderborn University</a>
    </footer>
    <!-- This part should be used in every page - End -->
    </div>
</template>
<script>
export default {
    
}
</script>
<style scoped>
    .background-color{
    background-color:white;
}

.nav-text{
    font-variant: small-caps;
    color: whitesmoke;
}

.nav-item:hover{
    background-color: rgb(199, 5, 5);
    box-shadow: 1px 1px 5px 3px #ffffff;
    border-radius: 3px;
}

.nav-item:hover .nav-text{
    font-size : 22px;
    color: #ffffff;
}

.navigation{
    background-image: linear-gradient(to right, rgb(235, 235, 235) 12.5%, #c20202 12.5%);
}    

.foot {
    background-color: #c20202;
    text-align: center;
    color: whitesmoke;
    font-size: 11px;
}

.footerLinks {
    color: whitesmoke;
    text-decoration: underline;
    font-size: 12px;
}

.footerLinks:hover{
    color: rgb(0, 0, 0);
}

.content {
    font-family: 'Roboto', sans-serif;
}

.about {
    background-color: rgb(87, 87, 87);
    color: whitesmoke;
}

</style>