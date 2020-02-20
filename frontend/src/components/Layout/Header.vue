<template>
    <v-container style="padding:0 !important; background-color: white;">  
        <v-img max-height="80" max-width="200" :src="require('../../assets/Logo.png')"></v-img>
    </v-container>
</template>
<script>
export default {
    
}
</script>
<style scoped>
    #bar {
        background-color: white !important;
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
        height:62px;
        width:160px;
    }
</style>