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
    background-color: whitesmoke;
    text-align: center;
    color: black;
    font-size: 11px;
}

.footerLinks {
    color: black;
    text-decoration: underline;
    font-size: 12px;
}

.footerLinks:hover{
    color: red;
}
</style>