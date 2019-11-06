<template>
    <div class="component">
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
                                <a class="nav-link" v-bind:href="Home"><h5 class="nav-text">Home</h5></a>
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
    </div>
</template>
<script>
export default {
    data: {
        Home: '..\src\components\landingPage\LandingPage.vue'
    }
}
</script>
<style scoped>
    

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
    color: #ffffff;
}

.navigation{
    background-image: linear-gradient(to right, rgb(235, 235, 235) 12.5%, #c20202 12.5%);
}    
</style>