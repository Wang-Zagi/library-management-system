<template>
  <div style="overflow-y:hidden;overflow-x: hidden">
    <router-view />
  </div>
</template>

<style>
@media print {
  body * {
    visibility: hidden;
  }
  .print-area{
    * {
      visibility: visible;
    }
    position: fixed;
    top: 0;
    left: 0;
    margin: 0 50px;
    display: inline-block;
    .barcode{
      text-align: center;
      display: inline-block;
      margin: 20px 20px;
    }
  }
}
</style>

<script>
export default {
  name:"App",
}
</script>