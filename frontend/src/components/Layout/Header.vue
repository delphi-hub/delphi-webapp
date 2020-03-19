<template>
    <nav class="navbar navbar-light bg-light justify-content-between" id="bar">
        <div id="barDiv">
            <a href="home">
                <img :src="require('../../assets/Logo.png')" id="logoDelphi">
            </a>
        </div>  
        <div id="item">
            <a href="#" id="itemA"><h5 class="text">About</h5></a>
        </div >
    </nav>
</template>
<script>
export default {
    
}
</script>
<style scoped>
    #bar {
        background-color: #c20202 !important;
        padding:0 !important;
    }
    #barDiv {
        width: 200px;
        height: 80px;
        background-color: rgb(235, 236, 237);
        text-align: center;
        padding-top: 13px;
    }
    .text{
        font-variant: small-caps;
        color: whitesmoke;
        padding:10px;
    }
    #item {
        margin-right: 3%;
    }
    #itemA {
        text-decoration: none;
    }
    #item:hover{
        background-color: rgb(199, 5, 5);
        box-shadow: 1px 1px 5px 3px #ffffff;
        border-radius: 3px;
    }
    #logoDelphi {
        height:55px;
        width:150px;
    }
</style>