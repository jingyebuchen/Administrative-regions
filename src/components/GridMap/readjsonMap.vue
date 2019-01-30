<template>
  <div class="grid_map" id="map">
    <!-- 左下角鼠标所在经纬度 -->
    <div id="mouse-position"></div>
    <!-- 格子弹框覆盖物 -->
    <div id="popup" class="ol-popup">
      <div id="popup-content"></div>
    </div>
    <!-- 顶部配置信息 -->
    <div class="unit">边长:</div>
    <div class="dropdown">
      <el-select v-model="value" placeholder="1000m" size='small' @change="ddchage(value)">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
    </div>
    <div class="b_type">边样式:</div>
    <div class="b_dropdown">
      <el-select v-model="b_value" placeholder="实线" size='small' @change="b_ddchage(b_value)">
        <el-option v-for="item in b_options" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
    </div>
    <div class="font_color">字体颜色:</div>
    <div class="font_color_dropdown">
      <el-color-picker v-model="color1" show-alpha @change="f_c_chage(color1)"></el-color-picker>
    </div>
    <div class="border_color">边线颜色:</div>
    <div class="border_color_dropdown">
      <el-color-picker v-model="color2" show-alpha @change="b_c_chage(color2)"></el-color-picker>
    </div>
    <div class="fill_color">填充颜色:</div>
    <div class="fill_color_dropdown">
      <el-color-picker v-model="color3" show-alpha @change="fill_c_chage(color3)"></el-color-picker>
    </div>
    <div class="border_weight">边线粗细:</div>
    <div class="b_weight_dropdown">
      <el-select v-model="border_weight" placeholder="1px" size='small' @change="b_w_ddchage(border_weight)">
        <el-option v-for="item in b_w_options" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
    </div>
    <div class="font_size">字体大小:</div>
    <div class="f_size_dropdown">
      <el-select v-model="font_size" placeholder="bold 12px Times New Roman" size='small' @change="f_s_ddchage(font_size)">
        <el-option v-for="item in f_s_options" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
    </div>
    <!-- 上下格开关 -->
    <div class="switch">
      <el-switch v-model="offon" active-color="#13ce66" inactive-color="#ff4949" @change="offon_s()">
      </el-switch>
    </div>
    <!-- 左侧数据列表 -->
    <div class="borderlist">
      <div class="data_list">
        <div class="list" v-for="item in createGridData">
          <div class="list_t">
            <div class="bh">{{item.gid}}</div>
            <div class="sl">
              {{item.sum}}
            </div>
          </div>
          <div class="list_b">
            <div class="list_b_1" v-for="a in item.data">
              <div class="mc">{{a.type}}</div>
              <div class="mcsl">{{a.num}}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Map from 'ol/Map.js';
  import View from 'ol/View.js';
  import TileLayer from 'ol/layer/Tile.js';
  import TileWMS from 'ol/source/TileWMS.js';
  import XYZSource from 'ol/source/XYZ';
  import Graticule from 'ol/Graticule.js';
  import {
    defaults as defaultControls,
    ScaleLine,
    FullScreen,
    MousePosition
  } from 'ol/control.js';
  import {
    createStringXY
  } from 'ol/coordinate.js';
  import {
    fromLonLat,
    transform,
    toLonLat
  } from 'ol/proj';
  import {
    Circle as CircleStyle,
    Fill,
    Stroke,
    Icon,
    Text,
    Style
  } from 'ol/style.js';
  import {
    Vector as VectorLayer
  } from 'ol/layer.js';
  import {
    OSM,
    Vector as VectorSource
  } from 'ol/source.js';
  import Feature from 'ol/Feature.js';
  import Point from 'ol/geom/Point.js';
  import {
    LineString,
    Polygon
  } from 'ol/geom.js';
  import {
    getDistance
  } from 'ol/sphere.js';
  import Draw, {
    createRegularPolygon,
    createBox
  } from 'ol/interaction/Draw.js';
  import {
    defaults as defaultInteractions,
    Select,
    Translate
  } from 'ol/interaction.js';
  import {
    click,
    pointerMove,
    altKeyOnly
  } from 'ol/events/condition.js';
  import GeoJSON from 'ol/format/GeoJSON';
  import EsriJSON from 'ol/format/EsriJSON';
  import Overlay from 'ol/Overlay.js';

  let map; //全局map变量
  let featuregrid = []; //全局已处理格子变量
  let geojson = []; //全局未处理格子变量
  let jsonlayer = ''; //全局格子图层变量
  let styles = ''; //全局样式变量
  let op_style = ''; //全局格子样式变量
  let op_style_arr = []; //全局格子样式变量数组

  export default {
    name: 'GridMap',
    data() {
      return {
        //边长
        options: [{
          value: '500',
          label: '500m'
        }, {
          value: '1000',
          label: '1000m'
        }, {
          value: '5000',
          label: '5000m'
        }],
        value: '',
        //默认边长
        unit: 1000,
        //边样式
        b_options: [{
          value: '实线',
          label: '实线'
        }, {
          value: '虚线',
          label: '虚线'
        }],
        b_value: '',
        // 字体大小
        f_s_options: [{
          value: 'bold 12px Times New Roman',
          label: 'bold 12px Times New Roman'
        }, {
          value: 'bold 16px Times New Roman',
          label: 'bold 16px Times New Roman'
        }, {
          value: 'bold 20px Times New Roman',
          label: 'bold 20px Times New Roman'
        }],
        font_size: 'bold 12px Times New Roman',
        // 边线粗细
        b_w_options: [{
          value: '1',
          label: '1px'
        }, {
          value: '2',
          label: '2px'
        }, {
          value: '3',
          label: '3px'
        }],
        border_weight: '1',
        // 字体颜色
        color1: 'rgba(255,0,0,1)',
        // 边线颜色
        color2: 'rgba(255,0,0,1)',
        // 填充颜色
        color3: 'rgba(0,255,0,0.2)',
        //上下格开关
        offon: true,
        //左侧数据列表
        createGridData: '',
      }
    },
    mounted() {
      let _this = this;
      //初始化地图
      _this.initMap();
    },
    methods: {
      //地图上下格方法
      offon_s() {
        if (this.offon == false) {
          map.removeLayer(jsonlayer);
        } else {
          map.addLayer(jsonlayer);
        }
      },
      //设置边线粗细方法
      b_w_ddchage(value) {
        for (var i = 0; i < featuregrid.length; i++) {
          op_style_arr[i].stroke_.width_ = value;
          featuregrid[i].setStyle(op_style_arr[i]);
        }
      },
      //设置字体大小方法
      f_s_ddchage(value) {
        for (var i = 0; i < featuregrid.length; i++) {
          op_style_arr[i].text_.font_ = value;
          featuregrid[i].setStyle(op_style_arr[i]);
        }
      },
      //设置填充颜色方法
      fill_c_chage(value) {
        for (var i = 0; i < featuregrid.length; i++) {
          op_style_arr[i].fill_.color_ = value;
          featuregrid[i].setStyle(op_style_arr[i]);
        }
      },
      //设置边线颜色方法
      b_c_chage(value) {
        for (var i = 0; i < featuregrid.length; i++) {
          op_style_arr[i].stroke_.color_ = value;
          featuregrid[i].setStyle(op_style_arr[i]);
        }
      },
      //设置字体颜色方法
      f_c_chage(value) {
        for (var i = 0; i < featuregrid.length; i++) {
          op_style_arr[i].text_.fill_.color_ = value;
          featuregrid[i].setStyle(op_style_arr[i]);
        }
      },
      //设置边长方法
      ddchage(value) {
        this.unit = value;
        this.$options.methods.request(this.unit, this);
      },
      //设置边样式方法
      b_ddchage(value) {
        for (var i = 0; i < featuregrid.length; i++) {
          if (value == '实线') {
            op_style_arr[i].stroke_.lineDash_ = null;
            featuregrid[i].setStyle(op_style_arr[i])
          } else {
            op_style_arr[i].stroke_.lineDash_ = [1, 4];
            featuregrid[i].setStyle(op_style_arr[i])
          }
        }
      },
      //初始化地图方法
      initMap() {
        let _this = this;
        //鼠标坐标
        var mousePositionControl = new MousePosition({
          coordinateFormat: createStringXY(4), //坐标格式
          projection: 'EPSG:4326', //地图投影坐标系
          className: 'custom-mouse-position', //坐标信息显示样式
          // 显示鼠标位置信息的目标容器
          target: document.getElementById('mouse-position'),
          undefinedHTML: '&nbsp' //未定义坐标的标记
        });
        //全局样式变量   
        styles = {
          'grid': new Style({
            stroke: new Stroke({
              width: 3,
              color: [255, 0, 0, 0.8],
            }),
            fill: new Fill({
              color: 'rgba(0,255,255,0.4)'
            })
          }),
          'range': new Style({
            stroke: new Stroke({
              width: 3,
              color: [0, 255, 0, 1],
              lineDash: [0.5, 4]
            })
          }),
          'icon': new Style({
            image: new Icon({
              anchor: [0.5, 1],
              scale: 0.1,
              src: require('../../assets/logo.png')
            })
          }),
          'geoMarker': new Style({
            image: new CircleStyle({
              radius: 7,
              fill: new Fill({
                color: 'black'
              }),
              stroke: new Stroke({
                color: 'white',
                width: 2
              })
            })
          })
        };
        //图层变量
        var layers = [
          new TileLayer({
            // extent: extents.HuBei,
            title: "天地图注记",
            source: new XYZSource({
              url: "http://t2.tianditu.com/DataServer?T=vec_w&x={x}&y={y}&l={z}&tk=c2c3b8a03c3d2a2b8f4afea1227e6891"
            })
          }),
          new TileLayer({
            // extent: extents.HuBei,
            title: "天地图行政区划",
            source: new XYZSource({
              url: "http://t3.tianditu.com/DataServer?T=cva_w&x={x}&y={y}&l={z}&tk=c2c3b8a03c3d2a2b8f4afea1227e6891"
            })
          })
        ];
        //全局地图对象
        map = new Map({
          controls: defaultControls().extend([
            new FullScreen(), mousePositionControl
          ]),
          layers: layers,
          target: 'map',
          view: new View({
            projection: 'EPSG:4326',
            center: [106.54, 29.40],
            zoom: 10,
            maxZoom: 18
          })
        });
        // display popup on click
        var element = document.getElementById('popup');
        var content = document.getElementById('popup-content');
        var popup = new Overlay({
          element: element,
          positioning: 'bottom-center',
          stopEvent: false,
          offset: [0, 0]
        });
        map.addOverlay(popup);
        map.on('click', function (evt) {
          var feature = map.forEachFeatureAtPixel(evt.pixel,
            function (feature) {
              return feature;
            });
          if (feature) {
            element.style.display = "block";
            var id = feature.getId();
            feature.setStyle(onlystyle);
            var coordinates = feature.getGeometry().getFirstCoordinate();
            content.innerHTML = id;
            popup.setPosition(coordinates);
          } else {
            element.style.display = "none";
          }
        });
        //地图鼠标样式--位于feature之上
        map.on('pointermove', function (evt) {
          if (evt.dragging) {
            $(element).popover('destroy');
            return;
          }
          map.getTargetElement().style.cursor =
            map.hasFeatureAtPixel(evt.pixel) ? 'pointer' : '';
        });
        // map.on("moveend",function(e){
        //     map.removeLayer(jsonlayer);
        //     var zoom = map.getView().getZoom();  //获取当前地图的缩放级别
        //     console.log(zoom);
        // }); 

        //调用请求默认网格、列表数据方法
        this.$options.methods.request(1000, this);
      },
      //请求网格、列表数据
      request(a, b) {
        b.b_value = '实线';
        b.color1 = 'rgba(255,0,0,1)';
        b.color2 = 'rgba(255,0,0,1)';
        b.color3 = 'rgba(0,255,0,0.2)';
        b.font_size = 'bold 12px Times New Roman';
        b.border_weight = '1';
        map.removeLayer(jsonlayer);
        jsonlayer = '';
        featuregrid = [];
        op_style_arr = [];
        //请求网格
        b.$axios.post('http://192.168.124.50:8090/createGridByID', {
            gridID: 1,
            sideUnit: a
          })
          .then(function (response) {
            var x = JSON.parse(response.data.data).features;
            geojson = x;
            for (var i = 0; i < x.length; i++) {
              var y = new GeoJSON().readFeature(x[i]);
              y.setId('' + i + '');
              op_style = new Style({
                stroke: new Stroke({
                  color: '#ff0000',
                  width: 1
                }),
                fill: new Fill({
                  color: 'rgba(0,255,0,0.2)'
                }),
                text: new Text({
                  text: '编号:' + i + ' ' + '案件数:' + x[i].properties.num + '',
                  font: "bold 12px Times New Roman",
                  fill: new Fill({
                    color: '#f00'
                  }),
                })
              })
              op_style_arr.push(op_style);
              y.setStyle(op_style);
              featuregrid.push(y);
            }
            jsonlayer = new VectorLayer({
              source: new VectorSource({
                features: featuregrid,
                projection: 'EPSG:4326'
              }),
            })
            map.addLayer(jsonlayer);
          })
          .catch(function (error) {
            console.log(error);
          });
        //请求列表
        b.$axios.post('http://192.168.124.50:8090/createGridData', {
            gridID: 1,
            sideUnit: a
          })
          .then(function (response) {
            var x = response.data.data;
            for (var i = 0; i < x.length; i++) {
              var total = 0;
              for (var j = 0; j < x[i].data.length; j++) {
                total += x[i].data[j].num;
              }
              for (var h = 0; h < featuregrid.length; h++) {
                if (x[i].gid == featuregrid[h].values_.gid) {
                  x[i].gid = h;
                  op_style_arr[h].text_.text_ = '编号:' + h + ' ' + '案件数:' + total + '';
                  featuregrid[h].setStyle(op_style_arr[h]);
                }
              }
              x[i].sum = total;
            }

            function compare(property) {
              return function (a, b) {
                var value1 = a[property];
                var value2 = b[property];
                return value1 - value2;
              }
            }
            x.sort(compare('gid'));
            b.createGridData = x;
          })
          .catch(function (error) {
            console.log(error);
          });
      }
    },
    watch: {

    }
  }

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .grid_map {
    height: 100%;
    width: 100%;
  }

  #mouse-position {
    float: left;
    position: absolute;
    bottom: 5px;
    width: 200px;
    height: 20px;
    /* 将z-index设置为显示在地图上层 */
    z-index: 2000;
  }

  .dropdown {
    height: 4%;
    width: 5%;
    position: absolute;
    z-index: 2000;
    left: 5%;
    top: 1.5%;
  }

  .unit {
    position: absolute;
    z-index: 2000;
    left: 2.5%;
    top: 2.2%;
    font-size: 16px;
    font-weight: bold;
  }

  .b_type {
    position: absolute;
    z-index: 2000;
    left: 10.5%;
    top: 2.2%;
    font-size: 16px;
    font-weight: bold;
  }

  .b_dropdown {
    height: 4%;
    width: 5%;
    position: absolute;
    z-index: 2000;
    left: 14%;
    top: 1.5%;
  }

  .font_color {
    position: absolute;
    z-index: 2000;
    left: 20.5%;
    top: 2.2%;
    font-size: 16px;
    font-weight: bold;
  }

  .font_color_dropdown {
    position: absolute;
    z-index: 2000;
    left: 24%;
    top: 1.5%;
  }

  .border_color {
    position: absolute;
    z-index: 2000;
    left: 27.5%;
    top: 2.2%;
    font-size: 16px;
    font-weight: bold;
  }

  .border_color_dropdown {
    position: absolute;
    z-index: 2000;
    left: 31%;
    top: 1.5%;
  }

  .fill_color {
    position: absolute;
    z-index: 2000;
    left: 34.5%;
    top: 2.2%;
    font-size: 16px;
    font-weight: bold;
  }

  .fill_color_dropdown {
    position: absolute;
    z-index: 2000;
    left: 38%;
    top: 1.5%;
  }

  .font_size {
    position: absolute;
    z-index: 2000;
    left: 50.5%;
    top: 2.2%;
    font-size: 16px;
    font-weight: bold;
  }

  .f_size_dropdown {
    position: absolute;
    z-index: 2000;
    left: 54.2%;
    top: 1.5%;
  }

  .border_weight {
    position: absolute;
    z-index: 2000;
    left: 41.5%;
    top: 2.2%;
    font-size: 16px;
    font-weight: bold;
  }

  .b_weight_dropdown {
    position: absolute;
    z-index: 2000;
    left: 45.2%;
    top: 1.5%;
    width: 4%;
  }

  .switch {
    position: absolute;
    z-index: 2000;
    left: 95%;
    top: 1.5%;
  }

  .borderlist {
    position: absolute;
    z-index: 2000;
    left: 2%;
    top: 7%;
    width: 14%;
    height: 90%;
    background: #fff;
    border-radius: 5px;
    padding: 1% 0;
  }

  .data_list {
    height: 100%;
    width: 100%;
    overflow: auto;
  }

  .list {
    height: 15%;
    width: 90%;
    border: 1px solid #000;
    margin-left: 4.5%;
    margin-top: 1.5%;
    margin-bottom: 1.5%;
  }

  .list_t {
    height: 40%;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .list_b {
    height: 60%;
    width: 100%;
    display: flex;
    flex-wrap: wrap;
  }

  .bh {
    font-size: 18px;
    width: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .sl {
    font-size: 28px;
    font-weight: bold;
    width: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .list_b_1 {
    height: 50%;
    width: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .mc {
    width: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .mcsl {
    width: 50%;
    color: teal;
    display: flex;
    justify-content: center;
    align-items: center;
  }

</style>
<style>
  /* 显示鼠标信息的自定义样式设置 */

  .custom-mouse-position {
    color: red;
    font-size: 16px;
    font-family: "微软雅黑";
  }

  .ol-popup {
    position: absolute;
    background-color: white;
    -webkit-filter: drop-shadow(0 1px 4px rgba(0, 0, 0, 0.2));
    filter: drop-shadow(0 1px 4px rgba(0, 0, 0, 0.2));
    padding: 15px;
    border-radius: 10px;
    border: 1px solid #cccccc;
    bottom: 12px;
    left: -35px;
    min-width: 50px;
  }

  .ol-popup:after,
  .ol-popup:before {
    top: 100%;
    border: solid transparent;
    content: " ";
    height: 0;
    width: 0;
    position: absolute;
    pointer-events: none;
  }

  .ol-popup:after {
    border-top-color: white;
    border-width: 10px;
    left: 50%;
    margin-left: -10px;
  }

  .ol-popup:before {
    border-top-color: #cccccc;
    border-width: 11px;
    left: 50%;
    margin-left: -11px;
  }

  .el-color-dropdown__link-btn {
    display: none !important;
  }

</style>
