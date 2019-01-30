<template>
  <div class="grid_map" id="map">
    <!-- 左下角鼠标所在经纬度 -->
    <div id="mouse-position"></div>
    <!-- 左下角放大镜 -->
    <div class="overview">
      <div class="over_view" id="overview">

      </div>
    </div>
    <!-- 顶部标题 -->
    <div class="title"></div>
    <!-- 托盘   -->
    <div class="tray">
      <div class="sentry">
        <div class="sentry_img"></div>
        <div class="sentry_text">岗亭</div>
      </div>
      <div class="person">
        <div class="person_img"></div>
        <div class="person_text">单兵</div>
      </div>
      <div class="car">
        <div class="car_img"></div>
        <div class="car_text">警车</div>
      </div>
      <div class="monitor">
        <div class="monitor_img"></div>
        <div class="monitor_text">监控</div>
      </div>
    </div>
    <div class="tray_bottom"></div>
    <!-- 岗亭展开 -->
    <div id="sentry_show" class="sentry_show">
      <div class="sentry_1">
        <div class="jk">
          <img src="../../assets/cellgrid/jk.png" alt="">
          <div class="jk_text">监控</div>
        </div>
      </div>
      <div class="sentry_2">
        <div class="dzwl">
          <img src="../../assets/cellgrid/dzwl.png" alt="">
          <div class="jk_text">电子</div>
          <div class="jk_text">围栏</div>
        </div>
      </div>
      <div class="sentry_3">
        <div class="fjjl">
          <img src="../../assets/cellgrid/fjjl.png" alt="">
          <div class="jk_text">附近</div>
          <div class="jk_text">警力</div>
        </div>
      </div>
      <div class="sentry_4">
        <div class="rlsb">
          <img src="../../assets/cellgrid/rlsb.png" alt="">
          <div class="jk_text">人脸</div>
        </div>
      </div>
      <div class="sentry_center"></div>
    </div>
    <!-- 单兵展开 -->
    <div id="police_show" class="police_show">
      <div class="police_content">
        <div class="police_photo">

        </div>
        <div class="police_name">
          <div class="name">
            <span></span>
            <div>姓名</div>
          </div>
          <div class="name_content">
            李晓明
          </div>
        </div>
        <div class="police_send">
          派遣
        </div>
        <div class="police_no">
            <span></span>
            <div>编号</div>
            <div>7894568</div>
        </div>
        <div class="group_no">
            <span></span>
            <div>组别</div>
            <div>应急处突组</div>
        </div>
        <div class="phone_no">
            <span></span>
            <div>电话</div>
            <div>13899982666</div>
        </div>
      </div>
    </div>
    <!-- 格子弹框覆盖物 -->
    <!-- <div id="popup" class="ol-popup">
      <div id="popup-content"></div>
    </div> -->
    <!-- 顶部配置信息 -->
    <!-- <div class="unit">边长:</div>
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
    </div> -->
    <!-- 上下格开关 -->
    <!-- <div class="switch">
      <el-switch v-model="offon" active-color="#13ce66" inactive-color="#ff4949" @change="offon_s()">
      </el-switch>
    </div> -->
    <!-- 左侧数据列表 -->
    <div class="listtitle"></div>
    <div class="borderlist">
      <div class="data_list nr_scroll">
        <div class="list" v-for="item in createGridData">
          <div class="list_time">
            <div>{{item.time}}</div>
            <div>{{item.date}}</div>
          </div>
          <div class="list_title"  v-bind:title="item.gid+item.cjdw">
            {{item.cjdw}}
          </div>
          <div class="list_mc" v-bind:title="item.mc">
            {{item.mc}}
          </div>     
          <div class="list_type">
            {{item.jqfl}}
          </div>        
          <!-- <div class="list_t">
            <div class="bh">{{item.gid}}</div>
            <div class="sl">
              {{item.num}}
            </div>
          </div>
          <div class="list_b">
            <div class="list_b_1" v-for="a in item.data">
              <div class="mc">{{a.type}}</div>
              <div class="mcsl">{{a.num}}</div>
            </div>
          </div> -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Map from 'ol_n/Map.js';
  import View from 'ol_n/View.js';
  import TileLayer from 'ol_n/layer/Tile.js';
  import TileWMS from 'ol/source/TileWMS.js';
  import XYZSource from 'ol_n/source/XYZ';
  import Graticule from 'ol/Graticule.js';
  import {
    defaults as defaultControls,
    ScaleLine,
    FullScreen,
    MousePosition,
    OverviewMap
  } from 'ol_s/control.js';
  import {
    createStringXY
  } from 'ol/coordinate.js';
  import {
    fromLonLat,
    transform,
    toLonLat
  } from 'ol_n/proj';
  import {
    Circle as CircleStyle,
    Fill,
    Stroke,
    Icon,
    Text,
    Style
  } from 'ol/style.js';
  import {
    Vector as VectorLayer,
    Group as LayerGroup
  } from 'ol_n/layer.js';
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
  import ImageLayer from 'ol_n/layer/Image.js';
  import Static from 'ol_n/source/ImageStatic.js'; 

  let map; //全局map变量
  let featuregrid = []; //全局已处理格子变量
  let geojson = []; //全局未处理格子变量
  let jsonlayer = ''; //全局格子图层变量
  let layer='';//全局图标图层变量
  let layergroups=[];
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
        //左下角控件
        var overviewMapControl = new OverviewMap({
            // see in overviewmap-custom.html to see the custom CSS used
            target: 'overview',
            className:'overviewmap',
            layers: [
              new TileLayer({
                className: 'bw',
                title: "天地图行政区划",
                source: new XYZSource({
                  url: "http://t2.tianditu.com/DataServer?T=vec_w&x={x}&y={y}&l={z}&tk=c2c3b8a03c3d2a2b8f4afea1227e6891"
                })
              }),
              new TileLayer({
                className: 'bw',
                title: "天地图注记",
                source: new XYZSource({
                  url: "http://t3.tianditu.com/DataServer?T=cva_w&x={x}&y={y}&l={z}&tk=c2c3b8a03c3d2a2b8f4afea1227e6891"
                })
              }),
            ],
            view: new View({
              projection: 'EPSG:4326',
              // center: [106.54, 29.40],
              center: [106.70, 29.36],
              zoom: 13,
              maxZoom: 18
            }),
            collapsible: false
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
            className: 'bw',
            title: "天地图行政区划",
            source: new XYZSource({
              url: "http://t2.tianditu.com/DataServer?T=vec_w&x={x}&y={y}&l={z}&tk=c2c3b8a03c3d2a2b8f4afea1227e6891"
            })
          }),
          new TileLayer({
            className: 'bw',
            title: "天地图注记",
            source: new XYZSource({
              url: "http://t3.tianditu.com/DataServer?T=cva_w&x={x}&y={y}&l={z}&tk=c2c3b8a03c3d2a2b8f4afea1227e6891"
            })
          })
        ];
        //全局地图对象
        map = new Map({
          controls: defaultControls().extend([
            new FullScreen(), mousePositionControl,overviewMapControl
          ]),
          layers: layers,
          target: 'map',
          view: new View({
            projection: 'EPSG:4326',
            // center: [106.54, 29.40],
            center: [106.70, 29.36],
            zoom: 13,
            maxZoom: 18
          })
        });
        // display popup on click
        var element = document.getElementById('sentry_show');
        var popup = new Overlay({
          element: element,
          positioning: 'bottom-center',
          stopEvent: true,
          className:'overlay',
          offset: [100, -100]
        });
        map.addOverlay(popup);
        // display popup on click
        var element1 = document.getElementById('police_show');
        var popup1 = new Overlay({
          element: element1,
          positioning: 'bottom-center',
          stopEvent: true,
          className:'overlay',
          offset: [455, -245]
        });
        map.addOverlay(popup1);
        map.on('click', function (evt) {
          var feature = map.forEachFeatureAtPixel(evt.pixel,
            function (feature) {
              return feature;
            });
            // console.log(feature);
          if (feature) {
            if(feature.get('name')=='sentry'){
              // alert('1');
            element.style.display = "block";
            var coordinates = feature.getGeometry().getCoordinates();
            popup.setPosition(coordinates);
            }
            if(feature.get('name')=='police'){
              // alert('1');
            element1.style.display = "block";
            var coordinates1 = feature.getGeometry().getCoordinates();
            feature.setStyle(new Style({
            image: new Icon( /** @type {module:ol/style/Icon~Options} */ ({
                anchor: [0.5, 0.6],
                crossOrigin: 'anonymous',
                src: require('../../assets/cellgrid/police_d.png'),
                scale: 1
              }))
            }));
            popup1.setPosition(coordinates1);
            }
          } else {
            element.style.display = "none";
            element1.style.display = "none";
            map.removeLayer(layer);
            _this.$options.methods.addpoint();
          }
          
        });
        //地图鼠标样式--位于feature之上
        map.on('pointermove', function (evt) {
          if (evt.dragging) {
            // element.style.display = "none";
            // element1.style.display = "none";
            return;
          }
          map.getTargetElement().style.cursor =
            map.hasFeatureAtPixel(evt.pixel) ? 'pointer' : '';
        });

        //调用请求默认网格、列表数据方法
        this.$options.methods.request(1000, this);
        this.$options.methods.addpoint();
      },
      //上点
      addpoint(){
        //岗亭图标
        var point00=[106.70, 29.36];
        var iconFeature = new Feature(new Point(point00));
        var point01=[106.60, 29.36];
        var iconFeature1 = new Feature(new Point(point01));
        function createStyle(src, img) {
          return new Style({
            image: new Icon( /** @type {module:ol/style/Icon~Options} */ ({
              anchor: [0.5, 0.5],
              crossOrigin: 'anonymous',
              src: src,
              img: img,
              scale: 1,
              imgSize: img ? [img.width, img.height] : undefined
            }))
          });
        }
        iconFeature.setStyle(createStyle(require('../../assets/cellgrid/sentry.png'), undefined));
        iconFeature.set('name','sentry');
        iconFeature1.setStyle(createStyle(require('../../assets/cellgrid/police.png'), undefined));
        iconFeature1.set('name','police');
        layer = new VectorLayer({
            zIndex:5,
            source: new VectorSource({
              features: [iconFeature,iconFeature1]
            })
          })
        map.addLayer(layer);
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
            var y=response.data.data.area;
            var regionLngLats = [];
            var coord3 = [
              []
            ];
            var regionArr = y.split(",");
            for (var m = 0; m < regionArr.length; m++) {
              var lnglatArr = regionArr[m].split(" ");
              var lnglat = [Number(lnglatArr[0]), Number(lnglatArr[1])];
              regionLngLats.push(lnglat);
            }
            var coord4 = [
              regionLngLats
            ];
            for (var i = 0; i < coord4[0].length; i++) {
              coord3[0].push(coord4[0][i]);
            }
            //===
            var polygon = new Polygon(coord3);
            var line_f = new Feature(polygon);
            line_f.setStyle(new Style({
              stroke: new Stroke({
                width: 5, color: [75,151,228, 1],
                // lineDash: [0.5, 4]
              })
            }));
            map.addLayer(new VectorLayer({
              source: new VectorSource({
                features: [line_f],
                projection: 'EPSG:4326'
              }),
            }));
            var x = JSON.parse(response.data.data.grid).features;
            geojson = x;
            for (var i = 0; i < x.length; i++) {
              var y = new GeoJSON().readFeature(x[i]);
              y.setId('' + i + '');
              op_style = new Style({
                stroke: new Stroke({
                  color: '#40a1c2',
                  width: 1
                }),
                // fill: new Fill({
                //   color: 'rgba(0,255,0,0.2)'
                // }),
                text: new Text({
                  text: '' + x[i].properties.num==0?'':x[i].properties.num + '',
                  // text: '' + x[i].properties.num + '',
                  font: '2em "arial", sans-serif',
                  fill: new Fill({
                    color: '#fff'
                  }),
                })
              })
              op_style_arr.push(op_style);
              y.setStyle(op_style);
              featuregrid.push(y);
            }
            // console.log(featuregrid);
            jsonlayer = new VectorLayer({
              source: new VectorSource({
                features: featuregrid,
                projection: 'EPSG:4326'
              }),
            })
            map.addLayer(jsonlayer);
            // var arr=[{index:517,color:'red'},{index:550,color:'orange'},{index:551,color:'orange'},{index:584,color:'blue'},{index:585,color:'red'}];
            // for(var i=0;i<arr.length;i++){
            //   var polylayer = new ImageLayer({
            //     source: new Static({
            //       url: require('../../assets/cellgrid/'+arr[i].color+'_grid.png'),
            //       projection: 'EPSG:4326',
            //       imageExtent: featuregrid[arr[i].index].getGeometry().getExtent()
            //     })
            //   })
            //     map.addLayer(polylayer);
            // }
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
            console.log(x);
            for (var i = 0; i < x.length; i++) {
              for (var h = 0; h < featuregrid.length; h++) {
                if (x[i].gid == featuregrid[h].values_.gid) {
                  x[i].gid = h;
                  op_style_arr[h].text_.text_ = '' +  x[i].num==0?'':x[i].num + '';
                  // op_style_arr[h].text_.text_ = h+'h' +  x[i].num+'';
                  featuregrid[h].setStyle(op_style_arr[h]);
                }
              }
              if(x[i].num<10){
                x[i].color='blue';
              }else if(x[i].num>=10&&x[i].num<=100){
                x[i].color='orange';
              }else{
                x[i].color='red';
              }
              layergroups.push(new ImageLayer({
                source: new Static({
                  url: require('../../assets/cellgrid/'+x[i].color+'_grid.png'),
                  projection: 'EPSG:4326',
                  imageExtent: featuregrid[x[i].gid].getGeometry().getExtent()
                })
              }))
              console.log('3');
            }
            let polylayer=new LayerGroup({
              layers: layergroups.slice(0,50)
            })
            map.addLayer(polylayer);
            console.log(x);
            console.log(polylayer);
          })
          .catch(function (error) {
            console.log(error);
          });
        b.$axios.post('http://192.168.124.50:8090/getGridDataDetail', {
            gridID: 1,
            sideUnit: a
          })
          .then(function (response) {
            var x = response.data.data.slice(0,50);
            for(var i=0;i<x.length;i++){
              for (var h = 0; h < featuregrid.length; h++) {
                if (x[i].gid == featuregrid[h].values_.gid) {
                  x[i].gid = h;
                }
              }      
              x[i].time=x[i].scbjsj.substr(8,2)+':'+x[i].scbjsj.substr(10,2);
              x[i].date=x[i].scbjsj.substr(4,2)+'-'+x[i].scbjsj.substr(6,2);
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
  .police_show{
    position: absolute;
    top: 15%;
    right: 25%;
    background: url('../../assets/cellgrid/police_bg.png') no-repeat;
    background-size: 100% 100%; 
    height: 271px;
    width: 462px;
    z-index: 2000;
  }
  .police_content{
    height: 70%;
    width: 70%;
    margin-top: 9%;
    margin-left: 22%;
  }
  .police_photo{
    height: 48%;
    width:28%;
    background: url('../../assets/cellgrid/photo_4.png') no-repeat;
    background-size: 100% 100%; 
    float: left;
  }
  .police_name{
    height: 48%;
    width:28%;
    background: url('../../assets/cellgrid/police_-.png') no-repeat left bottom;
    background-size: 100% 3%; 
    margin-left: 12%;
    float: left;
  }
  .name{
    width: 100%;
    height: 30%;
    margin-top: 24%;
  }
  .name span{
    width: 23%;
    height: 74%;
    background: url('../../assets/cellgrid/name.png') no-repeat;
    background-size: 100% 100%; 
    display: inline-block;
    float: left;
  }
  .name div{
    background: linear-gradient(to right, #677bff, #172478);
    -webkit-background-clip: text;
    color: transparent;
    float: left;
    margin-left: 7%;
    font-size: 18px;
  }
  .name_content{
    color: #fff;
    font-size: 18px;
    margin-top: 4%;
  }
  .police_no{
    width: 100%;
    height: 18%;
    float: left;
    align-items: center;
    display: flex;
    margin-left: 5%;
    margin-top: 2%;
  }
  .police_no span{
    width: 7%;
    height: 64%;
    background: url('../../assets/cellgrid/name.png') no-repeat;
    background-size: 100% 100%; 
    display: inline-block;
    float: left;
  }
  .police_no div:nth-child(2){
    background: linear-gradient(to right, #677bff, #172478);
    -webkit-background-clip: text;
    color: transparent;
    float: left;
    margin-left: 3%;
    font-size: 18px;
  }
  .police_no div:nth-child(3){
    color: #fff;
    font-size: 18px;
    margin-left: 14%;
  }
  .group_no{
    width: 100%;
    height: 18%;
    float: left;
    align-items: center;
    display: flex;
    margin-left: 5%;
  }
  .group_no span{
    width: 7%;
    height: 64%;
    background: url('../../assets/cellgrid/group.png') no-repeat;
    background-size: 100% 100%; 
    display: inline-block;
    float: left;
  }
  .group_no div:nth-child(2){
    background: linear-gradient(to right, #677bff, #172478);
    -webkit-background-clip: text;
    color: transparent;
    float: left;
    margin-left: 3%;
    font-size: 18px;
  }
  .group_no div:nth-child(3){
    color: #fff;
    font-size: 18px;
    margin-left: 14%;
  }
  .phone_no{
    width: 100%;
    height: 18%;
    float: left;
    align-items: center;
    display: flex;
    margin-left: 5%;
  }
  .phone_no span{
    width: 7%;
    height: 64%;
    background: url('../../assets/cellgrid/phone.png') no-repeat;
    background-size: 100% 100%; 
    display: inline-block;
    float: left;
  }
  .phone_no div:nth-child(2){
    background: linear-gradient(to right, #677bff, #172478);
    -webkit-background-clip: text;
    color: transparent;
    float: left;
    margin-left: 3%;
    font-size: 18px;
  }
  .phone_no div:nth-child(3){
    color: #fff;
    font-size: 18px;
    margin-left: 14%;
  }
  .police_send{
    height: 17%;
    width:26%;
    background: url('../../assets/cellgrid/send.png') no-repeat left bottom;
    background-size: 100% 100%; 
    margin-left: 2%;
    float: left;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 18px;
    color: #fff;
    cursor: pointer;
  }  
  .overview{
    position: absolute;
    bottom: 3%;
    left: 1%;
    z-index: 2000;
    height: 20%;
    width: 16%;
    background: url('../../assets/cellgrid/overview.png') no-repeat;
    background-size: 100% 100%; 
    padding: 1.5% 1% 0.8%;
  }
  .over_view{
    height: 100%;
    width: 100%;
  }
  .overlay{
    /* cursor: pointer; */
  }
  .sentry_show{
    position: absolute;
    top: 15%;
    right: 25%;
    background: conic-gradient(rgba(23,36,120,0.5),rgba(103,123,255,0.5));
    box-shadow: 0 0 10px rgba(23,36,120,0.87);
    height: 200px;
    width: 200px;
    z-index: 2000;
    border-radius: 50%;
    transform:rotate(45deg);
    overflow: hidden;
  }
  .sentry_1{
    height: 100px;
    width: 100px;
    float: left;
    border-right: 1px solid #000;
    border-bottom: 1px solid #000;
    border-top-left-radius: 50%;
    cursor:pointer;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .jk{
    transform:rotate(-45deg);
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    margin-top: 10px;
    margin-left: 10px;
  }
  .jk_text{
    color: #fff;
  }
  .sentry_2{
    height: 100px;
    width: 100px;
    float: right;
    border-left: 1px solid #000;
    border-bottom: 1px solid #000;
    cursor:pointer;
  }
  .dzwl{
    transform:rotate(-45deg);
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    margin-top: 15px;
    margin-left: -10px;
  }
  .sentry_3{
    height: 100px;
    width: 100px;
    float: left;
    border-right: 1px solid #000;
    border-top: 1px solid #000;
    cursor:pointer;
  }
  .fjjl{
    transform:rotate(-45deg);
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    margin-top: 10px;
  }
  .sentry_4{
    height: 100px;
    width: 100px;
    float: right;
    border-top: 1px solid #000;
    border-left: 1px solid #000;
    cursor:pointer;
  }
  .rlsb{
    transform:rotate(-45deg);
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    margin-top: 22px;
  }
  .sentry_center{
    height: 60px;
    width: 60px;
    background: url('../../assets/cellgrid/sentry_center.png') no-repeat;
    background-size: 100% 100%;  
    position: absolute;  
    top: 50%; 
    left: 50%;
    margin-top: -30px;
    margin-left: -30px;
    z-index: 2001;   
    transform:rotate(-45deg);
  }
  .title {
    height: 11%;
    width: 100%;
    background: url('../../assets/cellgrid/title.png') no-repeat center top;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 2000;
  }

  .tray {
    position: absolute;
    bottom: 26px;
    left: 50%;
    z-index: 2000;
    height: 12%;
    width: 41%;
    margin-left: -21%;
    /* background: red; */
    display: flex;
    justify-content: center;
  }
  .tray_bottom{
    position: absolute;
    bottom: -3px;
    left: 50%;
    height: 12%;
    width: 41%;
    margin-left: -21%;
    z-index: 1999;
    background: url('../../assets/cellgrid/tray.png') no-repeat;
    background-size: 100% 100%;    
  }
  .sentry {
    width: 20%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
  }

  .sentry_img {
    width: 68px;
    height: 68px;
    background: url('../../assets/cellgrid/t-sentry.png') no-repeat;
    background-size: 100% 100%;
  }

  .sentry_text {
    font-size: 22px;
    color: #fff;
    text-shadow: 0 0 0.2em #0b1fcc,
                -0 -0 0.2em #0b1fcc;
  }

  .person {
    width: 20%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
  }
  .person_img {
    width: 68px;
    height: 68px;
    background: url('../../assets/cellgrid/t-person.png') no-repeat;
    background-size: 100% 100%;
  }

  .person_text {
    font-size: 22px;
    color: #fff;
    text-shadow: 0 0 0.2em #0b1fcc,
                -0 -0 0.2em #0b1fcc;
  }
  .car {
    width: 20%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
  }
  .car_img {
    width: 68px;
    height: 68px;
    background: url('../../assets/cellgrid/t-car.png') no-repeat;
    background-size: 100% 100%;
  }

  .car_text {
    font-size: 22px;
    color: #fff;
    text-shadow: 0 0 0.2em #0b1fcc,
                -0 -0 0.2em #0b1fcc;
  }
  .monitor {
    width: 20%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
  }
  .monitor_img {
    width: 68px;
    height: 68px;
    background: url('../../assets/cellgrid/t-monitor.png') no-repeat;
    background-size: 100% 100%;
  }

  .monitor_text {
    font-size: 22px;
    color: #fff;
    text-shadow: 0 0 0.2em #0b1fcc,
                -0 -0 0.2em #0b1fcc;
  }
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

  .listtitle {
    position: absolute;
    z-index: 2000;
    width: 19%;
    height: 10%;
    right: 2%;
    top: 5%;
    background: url('../../assets/cellgrid/listtitle.png') no-repeat;
    background-size: 100% 100%;
  }

  .borderlist {
    position: absolute;
    z-index: 2000;
    right: 2%;
    top: 14%;
    width: 19%;
    height: 80%;
    border-radius: 5px;
    padding: 1% 0;
    background: url('../../assets/cellgrid/listbg.png') no-repeat;
    background-size: 100% 100%;
  }

  .data_list {
    height: 100%;
    width: 90%;
    overflow: auto;
    margin-left: 5%;
  }

  .list {
    /* height: 15%; */
    height: 108px;
    width: 94%;
    background: url('../../assets/cellgrid/list.png') no-repeat;
    background-size: 100% 100%;
    margin-left: 3.5%;
    margin-top: 1.5%;
    margin-bottom: 1.5%;
    color: #fff;
    position: relative;
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
  .list_time{
    width: 81px;
    height: 81px;
    background: url('../../assets/cellgrid/jq_bg.png') no-repeat center center;
    background-size: 90% 90%;
    position: absolute;
    top: 8%;
    left: 6%;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
  }
  .list_time div:nth-child(1){
    font-size: 18px;
  }
  .list_time div:nth-child(2){
    background: url('../../assets/cellgrid/jq_rq.png') no-repeat center center;
    background-size: 100% 100%;
  }
  .list_title{
    font-size: 18px;
    color: #a6d8f9;
    position: absolute;
    top: 15%;
    left: 35%;
    width: 57%;
    white-space: nowrap;  
    text-overflow:ellipsis; 
    overflow:hidden;
  }
  .list_mc{
    color: #5170b0;
    position: absolute;
    top: 40%;
    left: 35%;
    width: 57%;
    height: 40%;
    /* white-space: nowrap; */
    text-overflow:ellipsis; 
    overflow:hidden;
  }
  .list_type{
    width: 64px;
    height: 20px;
    background: url('../../assets/cellgrid/jq_type.png') no-repeat center center;
    background-size: 100% 100%;
    position: absolute;
    top: 72%;
    left: 76%;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
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
  .bw {
    filter: sepia(100%) invert(90%);
  }

  .ol-zoom,
  .ol-full-screen {
    display: none;
  }

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

  /*定义滚动条高宽及背景 高宽分别对应横竖滚动条的尺寸*/
  .nr_scroll::-webkit-scrollbar {
    width: 8px;
    height: 16px;
    background-color: rgba(58, 138, 145, 0);
  }

  .nr_scroll::scrollbar {
    width: 8px;
    height: 16px;
    background-color: rgba(58, 138, 145, 0);
  }

  /*定义滚动条轨道 内阴影+圆角*/
  .nr_scroll::-webkit-scrollbar-track {
    -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
    box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
    border-radius: 10px;
    background-color: rgba(58, 138, 145, 0);
    display: none;
  }

  .nr_scroll::scrollbar-track {
    -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
    box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
    border-radius: 10px;
    background-color: rgba(58, 138, 145, 0);
    display: none;
  }

  /*定义滑块 内阴影+圆角*/
  .nr_scroll::-webkit-scrollbar-thumb {
    border-radius: 10px;
    -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
    box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
    background-color: rgba(58, 138, 145, 0.5);
  }

  .nr_scroll::scrollbar-thumb {
    border-radius: 10px;
    -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
    box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
    background-color: rgba(58, 138, 145, 0.5);
  }
  .overviewmap{
    height: 79%;
    width: 87%;
    border:none!important;
  }
  .ol-overviewmap-map{
    height: 100%;
    width: 100%;
  }
  .overviewmap button{
    display: none;
  }
  .ol-control{
    background-color: rgba(64,161,194,.4);
    padding:1px;
  }
  .ol-overviewmap-box{
    cursor: all-scroll;
  }
</style>
