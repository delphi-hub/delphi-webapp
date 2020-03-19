<template>
    <div class="component">
        <footer class="fixed-bottom shadow foot">
        Delphi is maintained by the
        <a href="https://www.hni.uni-paderborn.de/en/software-engineering/" class="footerLinks">Secure Software Engineering group</a>
        at
        <a href="https://www.uni-paderborn.de/" class="footerLinks">Paderborn University</a>
    </footer>
    </div>
</template>
<script>
export default {
    
}
</script>
<style scoped>
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
</style>