<template>
  <div class="regional_linkage">
    <div class="link_top">地点选择器</div>
    <hr />
    <div class="link_m">
      <div class="link_m_l">
        <div class="city">
          <div class="title">城市</div>
          <div class="content">
            <!-- <v-distpicker type="mobile" province="湖北省" city="武汉市" area="江岸区"></v-distpicker> -->
            <div class="lists" v-for="(item,index) in city.child" v-on:click="addClassFun(item,index)" v-bind:class='{active:index==self}'>{{item.name}}</div>
          </div>
        </div>
        <div class="county">
          <div class="title">区县</div>
          <div class="content">
            <div class="lists" v-for="(item,index) in county" v-on:click="addClassFun_c(item,index)" v-bind:class='{active:index==self_c}'>{{item}}</div>
          </div>
        </div>
        <div class="police_station">
          <div class="title">派出所</div>
          <div class="ps_content">

          </div>
        </div>
      </div>
      <div class="link_m_r">
        <div class="top">
          <Input search enter-button="Search" placeholder="Enter administrative division..." @on-search="search()" v-model="search_content"/>
        </div>
        <div class="left"></div>
        <div class="right" id="map"></div>
      </div>
    </div>
    <div class="link_b">
      <Button type="primary" v-on:click="run()">确定</Button>
      <Button type="error" v-on:click="init()">关闭</Button>
    </div>
  </div>
</template>

<script>
  // import VDistpicker from 'v-distpicker'
  import json from '../../static/districts.json'

  import Map from 'ol/Map.js';
  import View from 'ol/View.js';
  import {
    defaults as defaultControls,
    ScaleLine
  } from 'ol/control.js';
  import TileLayer from 'ol/layer/Tile.js';
  import TileWMS from 'ol/source/TileWMS.js';
  import ImageWMS from 'ol/source/ImageWMS.js';
  import XYZSource from 'ol/source/XYZ';
  import {fromLonLat} from 'ol/proj';
  
	let map; 
	let zoom = 12; 
  let administrative;
  let area="湖北省";
  let province="";
  let city_w='';

  export default {
    name: 'RegionalLinkage',
    data() {
      return {
        city: '',
        county: '',
        self: 0,
        self_c: 0,
        search_content:''
      }
    },
    // components: {
    //   VDistpicker
    // }
    mounted() {
      let _this = this;
      _this.city = json[420000];
      _this.initMap();
      //初始化地图对象 
	     map=new T.Map("map"); 
    },
    methods: {
      addClassFun: function (item, index) {
        city_w="";
        province=item.name;
        this.county = item.child;
        this.self = index;
      },
      addClassFun_c: function (item,index) {
        this.self_c = index;
        city_w=item;
      },
      run(){
        map.clearOverLays();
        if(province){
          if(city_w){
            if(city_w=="市辖区"){
              area=province+city_w;
            }else{
              area=city_w;
            }
          }else{
            area=province;
          }
        }
        this.$options.methods.initMap();
      },
      init(){
        map.clearOverLays();
        area="湖北省";
        this.$options.methods.initMap();
      },
      search(){
        map.clearOverLays();
        area=this.search_content;
        this.$options.methods.initMap();
      },
      initMap() {
        // var layers = [
        //   new TileLayer({
        //     title: "天地图注记",
        //     source: new XYZSource({
        //       url: "http://t2.tianditu.com/DataServer?T=vec_w&x={x}&y={y}&l={z}"
        //     })
        //   }),
        //   new TileLayer({
        //     title: "天地图行政区划",
        //     source: new XYZSource({
        //       url:"http://t3.tianditu.com/DataServer?T=cva_w&x={x}&y={y}&l={z}"
        //     })
        //   }),
        //   new TileLayer({
        //     title: "天地图wms",
        //     source: new TileWMS({
        //       url:'http://gisserver.tianditu.gov.cn/tiandituService/wms',
        //       // projection: 'CGCS2000',
        //     })
        //   })
        // ];

        // map = new Map({
        //   layers: layers,
        //   target: 'map',
        //   view: new View({
        //     center: fromLonLat([114.3423400000, 30.5453900000]),
        //     zoom: 5
        //   })
        // });
	   	//设置显示地图的中心点和级别 
		// map.centerAndZoom(new T.LngLat(116.40969,39.89945),zoom); 
    administrative = new T.AdministrativeDivision();
        var config = {
          needSubInfo: false,
          needAll: false,
          needPolygon: true,
          needPre: true,
          searchType: 1,
          searchWord: area
        };
        administrative.search(config, searchResult);
        function searchResult(result)
        {
          if(result.getStatus() == 100) 
          {
            var data = result.getData();
            showMsg(data);
            // document.getElementById("administrativeMsg").innerHTML = html; 
          }
          else 
          {
            result.getMsg();
          }
        }
        
        function showMsg(data){
          for(var i = 0; i < data.length; i++){
            //解释上级行政区划
            if(data[i].parents){
              var upLevel = "";
              if(data[i].parents.country){
                upLevel += data[i].parents.country.name;
              }
              if(data[i].parents.province){
                upLevel += data[i].parents.province.name;
              }
            }
            if(data[i].points){
              //绘制行政区划
              polygon(data[i].points);
              console.log(data[i].points);
            }
            //解释下级行政区划
            if(data[i].child){
              showMsg(data[i].child);
              if(data[i].child.points){
                //绘制行政区划
                polygon(data[i].child.points);
              }
            }
          }
        }
        
        function polygon(points){
          var pointsArr = [];
              for (var i = 0; i < points.length; i++) {
                  var regionLngLats = [];
                  var regionArr = points[i].region.split(",");
                  for (var m = 0; m < regionArr.length; m++) {
                      var lnglatArr = regionArr[m].split(" ");
                      var lnglat = new T.LngLat(lnglatArr[0], lnglatArr[1]);
                      regionLngLats.push(lnglat);
                      pointsArr.push(lnglat);
                  }
                  //创建面对象
                var polygon = new T.Polygon(regionLngLats,{color: "red", weight: 3, opacity: 1, fillColor: "#00FF00", fillOpacity: 0.3});
                //向地图上添加行政区划面
                map.addOverLay(polygon);
              }
              //显示最佳比例尺
              map.setViewport(pointsArr);
        }
      }
    },
    watch: {

    }
  }

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .regional_linkage {
    height: 100%;
    width: 100%;
    padding: 0;
    margin: 0;
    font-size: 16px;
    overflow: auto;
    background-color: #999;
  }

  .link_top {
    width: 100%;
    height: 5%;
    text-indent: 10px;
    color: #fff;
    background-color: #666;
    display: flex;
    align-items: center;
  }

  .link_m {
    width: 100%;
    height: 88%;
    background-color: #ccc;
  }

  .link_m_l {
    width: 20%;
    height: 98%;
    border-right: 2px dashed #666;
    float: left;
    margin-top: 0.5%;
  }

  .link_m_r {
    width: 79%;
    height: 100%;
    float: left;
  }

  .top {
    height: 5%;
    width: 99%;
    margin-top: 10px;
    margin-left: 1%;
  }

  .left {
    float: left;
    height: 93%;
    margin-left: 1%;
    width: 35%;
    background: #fff;
  }

  .right {
    float: left;
    height: 93%;
    margin-left: 1%;
    width: 63%;
    background: #fff;
  }

  .link_b {
    width: 100%;
    height: 6.8%;
    display: flex;
    align-items: center;
  }

  .link_b button:nth-child(1) {
    margin-left: 90%;
  }

  .link_b button:nth-child(2) {
    margin-left: 1%;
  }

  .city {
    width: 100%;
    height: 30%;
  }

  .county {
    width: 100%;
    height: 30%;
  }

  .police_station {
    width: 100%;
    height: 40%;
  }

  .title {
    width: 56px;
    height: 32px;
    display: flex;
    justify-content: center;
    align-items: center;
    background: #ccc;
    border: 1px solid #000;
    border-radius: 5px;
    color: #000;
    margin-left: 4px;
  }

  .content {
    width: 95%;
    margin-left: 1%;
    padding: 1%;
    display: flex;
    flex-wrap: wrap;
  }

  .ps_content {
    width: 95%;
    height: 87%;
    margin-left: 1%;
    margin-top: 2%;
    background: #fff;
  }

  .lists {
    height: 24px;
    line-height: 24px;
    font-size: 14px;
    padding: 0 8px;
    margin-right: 4px;
    margin-top: 1%;
    background: #fff;
    background-size: 100% 100%;
    color: #000;
    cursor: pointer;
  }

  .active {
    background: #57a3f3;
    color: #fff;
  }
</style>
<style>
  .tdt-control-copyright{
    display: none!important;
  }
</style>

