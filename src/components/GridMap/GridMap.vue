<template>
  <div class="grid_map" id="map">
    <div id="mouse-position"></div>
  </div>
</template>

<script>
  import CommonFunc from './js/calclonlat'
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
    transform
  } from 'ol/proj';
  import {
    Circle as CircleStyle,
    Fill,
    Stroke,
    Icon,
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

  let map;

  export default {
    name: 'GridMap',
    data() {
      return {

      }
    },
    // components: {
    //   VDistpicker
    // }
    mounted() {
      let _this = this;
      _this.initMap();
    },
    methods: {
      initMap() {
        function createStyle(src, img) {
          return new Style({
            image: new Icon( /** @type {module:ol/style/Icon~Options} */ ({
              anchor: [0.5, 0.5],
              crossOrigin: 'anonymous',
              src: src,
              img: img,
              scale: 0.1,
              imgSize: img ? [img.width, img.height] : undefined
            }))
          });
        }
        var mousePositionControl = new MousePosition({
          coordinateFormat: createStringXY(4), //坐标格式
          projection: 'EPSG:4326', //地图投影坐标系
          className: 'custom-mouse-position', //坐标信息显示样式
          // 显示鼠标位置信息的目标容器
          target: document.getElementById('mouse-position'),
          undefinedHTML: '&nbsp' //未定义坐标的标记
        });
         var point00=[114.30, 30.60];
        //demo默认以1km为单位,画格从左至右,从上至下
        //同一经线上纬度差1°经线长约为111KM.
        //同一纬线圈上,经度差1°,其长约为111*cosαkm.（α为地理纬度）
        var point01 = CommonFunc.caculate.destinationVincenty(point00,90,1000);
        var point11 = CommonFunc.caculate.destinationVincenty(point01,180,1000);
        var point10 = CommonFunc.caculate.destinationVincenty(point00,180,1000);
        console.log(point00);
        console.log(point01);
        console.log(point11);
        console.log(point10);
        var point_1=[
          fromLonLat(point00),fromLonLat(point01),fromLonLat(point11),fromLonLat(point10),fromLonLat(point00)
        ];
        var styles = {
          'grid': new Style({
            stroke: new Stroke({
              width: 3, color: [255, 0, 0, 0.8],
              // lineDash: [0.5, 4]
            })
          }),
          'icon': new Style({
            image: new Icon({
              anchor: [0.5, 1],
              scale:0.1,
              src: require('../../assets/logo.png')
            })
          }),
          'geoMarker': new Style({
            image: new CircleStyle({
              radius: 7,
              fill: new Fill({color: 'black'}),
              stroke: new Stroke({
                color: 'white', width: 2
              })
            })
          })
        };
        var grid=new LineString(point_1);
        var gridFeature = new Feature({
          type: 'grid',
          geometry: grid
        });
        var iconFeature = new Feature(new Point(fromLonLat(point00)));
        iconFeature.setStyle(createStyle(require('../../assets/logo.png'), undefined));
        var layers = [
          new TileLayer({
            // extent: extents.HuBei,
            title: "天地图注记",
            source: new XYZSource({
              url: "http://t2.tianditu.com/DataServer?T=vec_w&x={x}&y={y}&l={z}"
            })
          }),
          new TileLayer({
            // extent: extents.HuBei,
            title: "天地图行政区划",
            source: new XYZSource({
              url: "http://t3.tianditu.com/DataServer?T=cva_w&x={x}&y={y}&l={z}"
            })
          }),
          new VectorLayer({
            source: new VectorSource({
              features: [iconFeature]
            })
          }),
          new VectorLayer({
          source: new VectorSource({
            features: [gridFeature]
          }),
          style: function(feature) {
            return styles[feature.get('type')];
          }
          }) 
        ];
        map = new Map({
          controls: defaultControls().extend([
            new FullScreen(), mousePositionControl
          ]),
          layers: layers,
          target: 'map',
          view: new View({
            center: fromLonLat([114.30, 30.60]),
            zoom: 10,
            maxZoom: 18
          })
        });
        // // Create the graticule component
        // var graticule = new Graticule({
        //   // the style to use for the lines, optional.
        //   strokeStyle: new Stroke({
        //     color: 'rgba(255,0,0,0.9)',
        //     width: 2,
        //     lineDash: [0.5, 4]
        //   }),
        //   showLabels: true
        // });

        // graticule.setMap(map);
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

</style>
<style>
  /* 显示鼠标信息的自定义样式设置 */

  .custom-mouse-position {
    color: red;
    font-size: 16px;
    font-family: "微软雅黑";
  }

</style>
