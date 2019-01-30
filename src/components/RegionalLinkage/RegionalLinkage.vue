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
          <Input search enter-button="Search" placeholder="Enter administrative division..." @on-search="search()"
            v-model="search_content" />
        </div>
        <div class="left"></div>
        <div class="right">
          <div id="map" class="map">
            <form class="form-inline">
              <label>Geometry type &nbsp;</label>
              <select id="type">
                <option value="None">None</option>
                <option value="Point">Point</option>
                <option value="LineString">LineString</option>
                <option value="Polygon">Polygon</option>
                <option value="Circle">Circle</option>
                <option value="Square">Square</option>
                <option value="Box">Box</option>
                <option value="Star">Star</option>
              </select>
            </form>
            <form class="form-inline_mea">
              <label>Measurement type &nbsp;</label>
              <select id="type_mea">
                <option value="None">None</option>
                <option value="length">Length (LineString)</option>
                <option value="area">Area (Polygon)</option>
              </select>
            </form>
            <div id="mouse-position"></div>
            <Button class="clear" @click="clear()">Clear</Button>
            <label for="speed" class="top10">
              speed:&nbsp;
              <input id="speed" type="range" min="1" max="999" step="10" value="1">
            </label>
            <button id="start-animation" class="top20">Start Animation</button>
          </div>
        </div>
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
  import json from '../../../static/json/districts.json'

  import Map from 'ol/Map.js';
  import View from 'ol/View.js';
  import {
    defaults as defaultControls,
    ScaleLine,
    FullScreen,
    MousePosition
  } from 'ol/control.js';
  import TileLayer from 'ol/layer/Tile.js';
  import TileWMS from 'ol/source/TileWMS.js';
  import ImageWMS from 'ol/source/ImageWMS.js';
  import XYZSource from 'ol/source/XYZ';
  import {
    fromLonLat,transform
  } from 'ol/proj';

  import Draw,{createRegularPolygon, createBox}  from 'ol/interaction/Draw.js';
  import {
    Vector as VectorLayer
  } from 'ol/layer.js';
  import {
    OSM,
    Vector as VectorSource
  } from 'ol/source.js';
  import {
    Circle as CircleStyle,
    Fill,
    Stroke,
    Icon,
    Style
  } from 'ol/style.js';
  import Feature from 'ol/Feature.js';
  import Point from 'ol/geom/Point.js';

  import {
    unByKey
  } from 'ol/Observable.js';
  import Overlay from 'ol/Overlay.js';
  import {
    getArea,
    getLength
  } from 'ol/sphere.js';
  import {
    LineString,
    Polygon
  } from 'ol/geom.js';
  import {createStringXY} from 'ol/coordinate.js';

  import {getCenter} from 'ol/extent.js';
  import ImageLayer from 'ol/layer/Image.js';
  import Projection from 'ol/proj/Projection.js';
  import Static from 'ol/source/ImageStatic.js';  

  import {transformExtent} from 'ol/proj.js';
  import TileJSON from 'ol/source/TileJSON.js';

  import Polyline from 'ol/format/Polyline.js';

  let map;
  let zoom = 12;
  let administrative;
  let area = "湖北省";
  let province = "";
  let city_w = '';
  let draw; // global so we can remove it later
  let drawmea;
  let source;
  let sourcemea;
  let helpTooltip;
  let measureTooltip;

  export default {
    name: 'RegionalLinkage',
    data() {
      return {
        city: '',
        county: '',
        self: 0,
        self_c: 0,
        search_content: ''
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
      //  map=new T.Map("map"); 
    },
    methods: {
      addClassFun: function (item, index) {
        city_w = "";
        province = item.name;
        this.county = item.child;
        this.self = index;
      },
      addClassFun_c: function (item, index) {
        this.self_c = index;
        city_w = item;
      },
      run() {
        // map.clearOverLays();
        // if(province){
        //   if(city_w){
        //     if(city_w=="市辖区"){
        //       area=province+city_w;
        //     }else{
        //       area=city_w;
        //     }
        //   }else{
        //     area=province;
        //   }
        // }
        // this.$options.methods.initMap();
      },
      init() {
        // map.clearOverLays();
        // area="湖北省";
        // this.$options.methods.initMap();
      },
      search() {
        // map.clearOverLays();
        // area=this.search_content;
        // this.$options.methods.initMap();
      },
      initMap() {
        var coord1=[
          fromLonLat([104.06,30.67]),fromLonLat([110.28,30.54]),fromLonLat([116.28,39.54]),fromLonLat([117.28,38.54])
        ];//线
      var polyline = [
        'hldhx@lnau`BCG_EaC??cFjAwDjF??uBlKMd@}@z@??aC^yk@z_@se@b[wFdE??wFfE}N',
        'fIoGxB_I\\gG}@eHoCyTmPqGaBaHOoD\\??yVrGotA|N??o[N_STiwAtEmHGeHcAkiA}^',
        'aMyBiHOkFNoI`CcVvM??gG^gF_@iJwC??eCcA]OoL}DwFyCaCgCcCwDcGwHsSoX??wI_E',
        'kUFmq@hBiOqBgTwS??iYse@gYq\\cp@ce@{vA}s@csJqaE}{@iRaqE{lBeRoIwd@_T{]_',
        'Ngn@{PmhEwaA{SeF_u@kQuyAw]wQeEgtAsZ}LiCarAkVwI}D??_}RcjEinPspDwSqCgs@',
        'sPua@_OkXaMeT_Nwk@ob@gV}TiYs[uTwXoNmT{Uyb@wNg]{Nqa@oDgNeJu_@_G}YsFw]k',
        'DuZyDmm@i_@uyIJe~@jCg|@nGiv@zUi_BfNqaAvIow@dEed@dCcf@r@qz@Egs@{Acu@mC',
        'um@yIey@gGig@cK_m@aSku@qRil@we@{mAeTej@}Tkz@cLgr@aHko@qOmcEaJw~C{w@ka',
        'i@qBchBq@kmBS{kDnBscBnFu_Dbc@_~QHeU`IuyDrC_}@bByp@fCyoA?qMbD}{AIkeAgB',
        'k_A_A{UsDke@gFej@qH{o@qGgb@qH{`@mMgm@uQus@kL{_@yOmd@ymBgwE}x@ouBwtA__',
        'DuhEgaKuWct@gp@cnBii@mlBa_@}|Asj@qrCg^eaC}L{dAaJ_aAiOyjByH{nAuYu`GsAw',
        'Xyn@ywMyOyqD{_@cfIcDe}@y@aeBJmwA`CkiAbFkhBlTgdDdPyiB`W}xDnSa}DbJyhCrX',
        'itAhT}x@bE}Z_@qW_Kwv@qKaaAiBgXvIm}A~JovAxCqW~WanB`XewBbK{_A`K}fBvAmi@',
        'xBycBeCauBoF}}@qJioAww@gjHaPopA_NurAyJku@uGmi@cDs[eRaiBkQstAsQkcByNma',
        'CsK_uBcJgbEw@gkB_@ypEqDoqSm@eZcDwjBoGw`BoMegBaU_`Ce_@_uBqb@ytBwkFqiT_',
        'fAqfEwe@mfCka@_eC_UmlB}MmaBeWkkDeHwqAoX}~DcBsZmLcxBqOwqE_DkyAuJmrJ\\o',
        '~CfIewG|YibQxBssB?es@qGciA}RorAoVajA_nAodD{[y`AgPqp@mKwr@ms@umEaW{dAm',
        'b@umAw|@ojBwzDaaJsmBwbEgdCsrFqhAihDquAi`Fux@}_Dui@_eB_u@guCuyAuiHukA_',
        'lKszAu|OmaA{wKm}@clHs_A_rEahCssKo\\sgBsSglAqk@yvDcS_wAyTwpBmPc|BwZknF',
        'oFscB_GsaDiZmyMyLgtHgQonHqT{hKaPg}Dqq@m~Hym@c`EuiBudIabB{hF{pWifx@snA',
        'w`GkFyVqf@y~BkoAi}Lel@wtc@}`@oaXi_C}pZsi@eqGsSuqJ|Lqeb@e]kgPcaAu}SkDw',
        'zGhn@gjYh\\qlNZovJieBqja@ed@siO{[ol\\kCmjMe\\isHorCmec@uLebB}EqiBaCg}',
        '@m@qwHrT_vFps@kkI`uAszIrpHuzYxx@e{Crw@kpDhN{wBtQarDy@knFgP_yCu\\wyCwy',
        'A{kHo~@omEoYmoDaEcPiuAosDagD}rO{{AsyEihCayFilLaiUqm@_bAumFo}DgqA_uByi',
        '@swC~AkzDlhA}xEvcBa}Cxk@ql@`rAo|@~bBq{@``Bye@djDww@z_C_cAtn@ye@nfC_eC',
        '|gGahH~s@w}@``Fi~FpnAooC|u@wlEaEedRlYkrPvKerBfYs}Arg@m}AtrCkzElw@gjBb',
        'h@woBhR{gCwGkgCc[wtCuOapAcFoh@uBy[yBgr@c@iq@o@wvEv@sp@`FajBfCaq@fIipA',
        'dy@ewJlUc`ExGuaBdEmbBpBssArAuqBBg}@s@g{AkB{bBif@_bYmC}r@kDgm@sPq_BuJ_',
        's@{X_{AsK_d@eM{d@wVgx@oWcu@??aDmOkNia@wFoSmDyMyCkPiBePwAob@XcQ|@oNdCo',
        'SfFwXhEmOnLi\\lbAulB`X_d@|k@au@bc@oc@bqC}{BhwDgcD`l@ed@??bL{G|a@eTje@',
        'oS~]cLr~Bgh@|b@}Jv}EieAlv@sPluD{z@nzA_]`|KchCtd@sPvb@wSb{@ko@f`RooQ~e',
        '[upZbuIolI|gFafFzu@iq@nMmJ|OeJn^{Qjh@yQhc@uJ~j@iGdd@kAp~BkBxO{@|QsAfY',
        'gEtYiGd]}Jpd@wRhVoNzNeK`j@ce@vgK}cJnSoSzQkVvUm^rSgc@`Uql@xIq\\vIgg@~k',
        'Dyq[nIir@jNoq@xNwc@fYik@tk@su@neB}uBhqEesFjoGeyHtCoD|D}Ed|@ctAbIuOzqB',
        '_}D~NgY`\\um@v[gm@v{Cw`G`w@o{AdjAwzBh{C}`Gpp@ypAxn@}mAfz@{bBbNia@??jI',
        'ab@`CuOlC}YnAcV`@_^m@aeB}@yk@YuTuBg^uCkZiGk\\yGeY}Lu_@oOsZiTe[uWi[sl@',
        'mo@soAauAsrBgzBqgAglAyd@ig@asAcyAklA}qAwHkGi{@s~@goAmsAyDeEirB_{B}IsJ',
        'uEeFymAssAkdAmhAyTcVkFeEoKiH}l@kp@wg@sj@ku@ey@uh@kj@}EsFmG}Jk^_r@_f@m',
        '~@ym@yjA??a@cFd@kBrCgDbAUnAcBhAyAdk@et@??kF}D??OL'
      ].join('');

      // var route = /** @type {module:ol/geom/LineString~LineString} */ (new Polyline({
      //   factor: 1e6
      // }).readGeometry(polyline, {
      //   dataProjection: 'EPSG:4326',
      //   featureProjection: 'EPSG:3857'
      // }));
      var route=new LineString(coord1);
var routeCoords = route.getCoordinates();
      var routeLength = routeCoords.length;

      var routeFeature = new Feature({
        type: 'route',
        geometry: route
      });
      var geoMarker = new Feature({
        type: 'geoMarker',
        geometry: new Point(routeCoords[0])
      });
      var startMarker = new Feature({
        type: 'icon',
        geometry: new Point(routeCoords[0])
      });
      var endMarker = new Feature({
        type: 'icon',
        geometry: new Point(routeCoords[routeLength - 1])
      });

      var styles = {
        'route': new Style({
          stroke: new Stroke({
            width: 6, color: [237, 212, 0, 0.8]
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
var animating = false;
      var speed, now;
      var speedInput = document.getElementById('speed');
      var startButton = document.getElementById('start-animation');
      var center = fromLonLat([104.06,30.67]);
      var moveFeature = function(event) {
        var vectorContext = event.vectorContext;
        var frameState = event.frameState;

        if (animating) {
          var elapsedTime = frameState.time - now;
          // here the trick to increase speed is to jump some indexes
          // on lineString coordinates
          var index = Math.round(speed * elapsedTime / 1000);

          if (index >= routeLength) {
            stopAnimation(true);
            return;
          }
          var currentPoint = new Point(routeCoords[index]);
          var feature = new Feature(currentPoint);
          vectorContext.drawFeature(feature, styles.geoMarker);
        }
        // tell OpenLayers to continue the postcompose animation
        map.render();
      };

      function startAnimation() {
        if (animating) {
          stopAnimation(false);
        } else {
          animating = true;
          now = new Date().getTime();
          speed = speedInput.value;
          startButton.textContent = 'Cancel Animation';
          // hide geoMarker
          geoMarker.setStyle(null);
          // just in case you pan somewhere else
          map.getView().setCenter(center);
          map.on('postcompose', moveFeature);
          map.render();
        }
      }


      /**
       * @param {boolean} ended end of animation.
       */
      function stopAnimation(ended) {
        animating = false;
        startButton.textContent = 'Start Animation';

        // if animation cancelled set the marker at the beginning
        var coord = ended ? routeCoords[routeLength - 1] : routeCoords[0];
        /** @type {module:ol/geom/Point~Point} */ (geoMarker.getGeometry())
          .setCoordinates(coord);
        //remove listener
        map.un('postcompose', moveFeature);
      }

      startButton.addEventListener('click', startAnimation, false);
      // var extent = [0, 0, 1024, 968];
      // var projection = new Projection({
      //   code: 'xkcd-image',
      //   units: 'pixels',
      //   extent: extent
      // });

        var linestyle=new Style({
            stroke: new Stroke({
              color: '#6A5ACD',
              width: 2
            }),
            fill: new Fill({
              color: 'rgba(135,206,250,0.2)'
            })
        })
        var coord2=[[
          [104.06,30.67],[116.28,39.54],[117.28,38.54]
        ]];//面
        var coord3=[[]];
        var points="110.989 33.253,111.174 33.117,111.283 32.862,111.574 32.591,111.712 32.608,111.873 32.506,112.306 32.328,112.54 32.406,112.762 32.346,112.868 32.399,113.112 32.378,113.205 32.434,113.422 32.272,113.606 32.363,113.733 32.258,113.854 31.842,114.288 31.754,114.581 31.764,114.639 31.582,114.825 31.461,115.119 31.6,115.205 31.447,115.368 31.407,115.579 31.145,115.707 31.2,116.026 31.011,115.862 30.868,115.758 30.687,115.915 30.518,115.88 30.382,116.053 30.217,116.131 29.825,115.961 29.727,115.663 29.853,115.506 29.843,115.354 29.649,115.226 29.654,115.119 29.512,114.976 29.567,114.864 29.408,114.509 29.326,114.254 29.346,113.937 29.05,113.68 29.09,113.604 29.254,113.734 29.592,113.658 29.687,113.356 29.69,113.139 29.452,112.906 29.61,112.926 29.764,112.645 29.595,112.433 29.635,112.364 29.545,111.909 29.855,111.73 29.924,111.546 29.898,111.237 30.043,110.925 30.066,110.764 30.123,110.487 30.022,110.63 29.763,110.368 29.636,110.114 29.793,109.777 29.761,109.702 29.61,109.524 29.628,109.425 29.53,109.228 29.122,109.106 29.219,109.061 29.403,108.916 29.329,108.882 29.636,108.739 29.659,108.598 29.867,108.514 29.885,108.578 30.259,108.41 30.481,108.694 30.549,108.839 30.509,108.963 30.627,109.146 30.53,109.356 30.553,109.898 30.903,110.146 30.914,110.115 31.092,110.159 31.305,109.919 31.524,109.741 31.58,109.581 31.729,109.635 31.819,109.555 32.221,109.555 32.221,109.498 32.393,109.62 32.599,110.124 32.619,110.096 32.836,109.798 32.879,109.793 32.88,109.787 33.012,109.787 33.013,109.618 33.272,109.619 33.273,110.208 33.164,110.466 33.183,110.707 33.097,110.989 33.253";
            var regionLngLats = [];
            var regionArr = points.split(",");
            for (var m = 0; m < regionArr.length; m++) {
                var lnglatArr = regionArr[m].split(" ");
                var lnglat = [Number(lnglatArr[0]), Number(lnglatArr[1])];
                regionLngLats.push(lnglat);
            }
        var coord4=[
          regionLngLats
        ];
        for(var i=0;i<coord4[0].length;i++){
          coord3[0].push(fromLonLat(coord4[0][i]));
        }
        //     var coord = transform(evt.coordinate, 'EPSG:3857', 'EPSG:4326');
        // var line_f=new Feature(new LineString(coord1));
        console.log(coord3);
        var line_f=new Feature(new Polygon(coord3));
        line_f.setStyle(linestyle);

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

        var iconFeature = new Feature(new Point(fromLonLat([114.3423400000, 30.5453900000])));
        iconFeature.setStyle(createStyle(require('../../assets/logo.png'), undefined));
        source = new VectorSource({
          wrapX: false
        });
        sourcemea = new VectorSource();
        function transform2(extent) {
          return transformExtent(extent, 'EPSG:4326', 'EPSG:3857');
        }
        var extents = {
          HuBei: transform2([108.4000, 29.1294, 116.1454, 33.2628]),
        };
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
            source: source,
            style: new Style({
              fill: new Fill({
                color: 'rgba(255, 255, 255, 0.2)'
              }),
              stroke: new Stroke({
                color: '#ff0000',
                width: 2
              }),
              image: new CircleStyle({
                radius: 7,
                fill: new Fill({
                  color: '#ff0000'
                })
              })
            })
          }),
          new VectorLayer({
            source: new VectorSource({
              features: [iconFeature]
            })
          }),
          new VectorLayer({
            source: sourcemea,
            style: new Style({
              fill: new Fill({
                color: 'rgba(255, 255, 255, 0.2)'
              }),
              stroke: new Stroke({
                color: '#ffcc33',
                width: 2
              }),
              image: new CircleStyle({
                radius: 7,
                fill: new Fill({
                  color: '#ffcc33'
                })
              })
            })
          }),
          new VectorLayer({
            source: new VectorSource({
              features: [line_f],
              projection: 'EPSG:4326'
            })
          }),
          new ImageLayer({
            source: new Static({
              url: require('../../assets/colorlight.png'),
               projection: 'EPSG:3857',
              imageExtent: extents.HuBei
            })
          }),
          // new TileLayer({
          //   extent: extents.HuBei,
          //   source: new TileJSON({
          //     url: 'https://api.tiles.mapbox.com/v3/mapbox.world-black.json?secure',
          //     crossOrigin: 'anonymous'
          //   })
          // }),   
          new VectorLayer({
          source: new VectorSource({
            features: [routeFeature, geoMarker, startMarker, endMarker]
          }),
          style: function(feature) {
            // hide geoMarker if animation is active
            if (animating && feature.get('type') === 'geoMarker') {
              return null;
            }
            return styles[feature.get('type')];
          }
          })  
        ];
            // 实例化鼠标位置控件
        var stringifyFunc = createStringXY(4);
        var mousePositionControl = new MousePosition({
            coordinateFormat:createStringXY(4), //坐标格式
            projection: 'EPSG:4326', //地图投影坐标系
            className: 'custom-mouse-position', //坐标信息显示样式
            // 显示鼠标位置信息的目标容器
            target: document.getElementById('mouse-position'),
            undefinedHTML: '&nbsp' //未定义坐标的标记
        });
        /**
         * Currently drawn feature.
         * @type {module:ol/Feature~Feature}
         */
        var sketch;


        /**
         * The help tooltip element.
         * @type {Element}
         */
        var helpTooltipElement;


        /**
         * Overlay to show the help messages.
         * @type {module:ol/Overlay}
         */
        // var helpTooltip;


        /**
         * The measure tooltip element.
         * @type {Element}
         */
        var measureTooltipElement;


        /**
         * Overlay to show the measurement.
         * @type {module:ol/Overlay}
         */
        // var measureTooltip;


        /**
         * Message to show when the user is drawing a polygon.
         * @type {string}
         */
        var continuePolygonMsg = 'Click to continue drawing the polygon';


        /**
         * Message to show when the user is drawing a line.
         * @type {string}
         */
        var continueLineMsg = 'Click to continue drawing the line';


        /**
         * Handle pointer move.
         * @param {module:ol/MapBrowserEvent~MapBrowserEvent} evt The event.
         */
        var pointerMoveHandler = function (evt) {
          if (evt.dragging) {
            return;
          }
          /** @type {string} */
          var helpMsg;
          if (typeSelectmea.value != "None") {
            helpMsg = 'Click to start drawing';

            if (sketch) {
              var geom = (sketch.getGeometry());
              if (geom instanceof Polygon) {
                helpMsg = continuePolygonMsg;
              } else if (geom instanceof LineString) {
                helpMsg = continueLineMsg;
              }
            }

            helpTooltipElement.innerHTML = helpMsg;
            helpTooltip.setPosition(evt.coordinate);
            helpTooltipElement.classList.remove('hidden');
          } else {
            helpMsg = '';
          }
        };
        map = new Map({
          controls: defaultControls().extend([
            new FullScreen(),mousePositionControl
          ]),
          layers: layers,
          target: 'map',
          view: new View({
            // projection: projection,
            // center: getCenter(extent),
            // zoom: 2,
            // maxZoom: 8,
            center: fromLonLat([114.3423400000, 30.5453900000]),
            zoom: 10,
            maxZoom: 18
          })
        });
        // 鼠标移动事件
        // map.on('pointermove', function (evt) {
        //     // 经纬度坐标
        //     var coord = transform(evt.coordinate, 'EPSG:3857', 'EPSG:4326');
        //     console.log(coord);
        // });
        var typeSelect = document.getElementById('type');
      function addInteraction() {
        var value = typeSelect.value;
        if (value !== 'None') {
          var geometryFunction;
          if (value === 'Square') {
            value = 'Circle';
            geometryFunction = createRegularPolygon(4);
          } else if (value === 'Box') {
            value = 'Circle';
            geometryFunction = createBox();
          } else if (value === 'Star') {
            value = 'Circle';
            geometryFunction = function(coordinates, geometry) {
              var center = coordinates[0];
              var last = coordinates[1];
              var dx = center[0] - last[0];
              var dy = center[1] - last[1];
              var radius = Math.sqrt(dx * dx + dy * dy);
              var rotation = Math.atan2(dy, dx);
              var newCoordinates = [];
              var numPoints = 12;
              for (var i = 0; i < numPoints; ++i) {
                var angle = rotation + i * 2 * Math.PI / numPoints;
                var fraction = i % 2 === 0 ? 1 : 0.5;
                var offsetX = radius * fraction * Math.cos(angle);
                var offsetY = radius * fraction * Math.sin(angle);
                newCoordinates.push([center[0] + offsetX, center[1] + offsetY]);
              }
              newCoordinates.push(newCoordinates[0].slice());
              if (!geometry) {
                geometry = new Polygon([newCoordinates]);
              } else {
                geometry.setCoordinates([newCoordinates]);
              }
              return geometry;
            };
          }
          draw = new Draw({
            source: source,
            type: value,
            geometryFunction: geometryFunction
          });
          map.addInteraction(draw);
        }
      }


        /**
         * Handle change event.
         */
        typeSelect.onchange = function () {
          map.removeInteraction(draw);
          addInteraction();
        };

        addInteraction();
        map.on('pointermove', pointerMoveHandler);

        map.getViewport().addEventListener('mouseout', function () {
          if (typeSelectmea.value != "None") {
            helpTooltipElement.classList.add('hidden');
          }
        });

        var typeSelectmea = document.getElementById('type_mea');

        //var draw; // global so we can remove it later


        /**
         * Format length output.
         * @param {module:ol/geom/LineString~LineString} line The line.
         * @return {string} The formatted length.
         */
        var formatLength = function (line) {
          var length = getLength(line);
          var output;
          if (length > 100) {
            output = (Math.round(length / 1000 * 100) / 100) +
              ' ' + 'km';
          } else {
            output = (Math.round(length * 100) / 100) +
              ' ' + 'm';
          }
          return output;
        };


        /**
         * Format area output.
         * @param {module:ol/geom/Polygon~Polygon} polygon The polygon.
         * @return {string} Formatted area.
         */
        var formatArea = function (polygon) {
          var area = getArea(polygon);
          var output;
          if (area > 10000) {
            output = (Math.round(area / 1000000 * 100) / 100) +
              ' ' + 'km<sup>2</sup>';
          } else {
            output = (Math.round(area * 100) / 100) +
              ' ' + 'm<sup>2</sup>';
          }
          return output;
        };

        function addInteractionmea() {
          if (typeSelectmea.value != "None") {
            var type = (typeSelectmea.value == 'area' ? 'Polygon' : 'LineString');
            drawmea = new Draw({
              source: sourcemea,
              type: type,
              style: new Style({
                fill: new Fill({
                  color: 'rgba(255, 255, 255, 0.2)'
                }),
                stroke: new Stroke({
                  color: 'rgba(0, 0, 0, 0.5)',
                  lineDash: [10, 10],
                  width: 2
                }),
                image: new CircleStyle({
                  radius: 5,
                  stroke: new Stroke({
                    color: 'rgba(0, 0, 0, 0.7)'
                  }),
                  fill: new Fill({
                    color: 'rgba(255, 255, 255, 0.2)'
                  })
                })
              })
            });
            map.addInteraction(drawmea);

            createMeasureTooltip();
            createHelpTooltip();

            var listener;
            drawmea.on('drawstart',
              function (evt) {
                // set sketch
                sketch = evt.feature;

                /** @type {module:ol/coordinate~Coordinate|undefined} */
                var tooltipCoord = evt.coordinate;

                listener = sketch.getGeometry().on('change', function (evt) {
                  var geom = evt.target;
                  var output;
                  if (geom instanceof Polygon) {
                    output = formatArea(geom);
                    tooltipCoord = geom.getInteriorPoint().getCoordinates();
                  } else if (geom instanceof LineString) {
                    output = formatLength(geom);
                    tooltipCoord = geom.getLastCoordinate();
                  }
                  measureTooltipElement.innerHTML = output;
                  measureTooltip.setPosition(tooltipCoord);
                });
              }, this);

            drawmea.on('drawend',
              function () {
                measureTooltipElement.className = 'tooltip tooltip-static';
                measureTooltip.setOffset([0, -7]);
                // unset sketch
                sketch = null;
                // unset tooltip so that a new one can be created
                measureTooltipElement = null;
                createMeasureTooltip();
                unByKey(listener);
              }, this);
          } else {
            map.removeOverlay(helpTooltip);
          }
        }


        /**
         * Creates a new help tooltip
         */
        function createHelpTooltip() {
          if (helpTooltipElement) {
            helpTooltipElement.parentNode.removeChild(helpTooltipElement);
          }
          helpTooltipElement = document.createElement('div');
          helpTooltipElement.className = 'tooltip hidden';
          helpTooltip = new Overlay({
            element: helpTooltipElement,
            offset: [15, 0],
            positioning: 'center-left'
          });
          map.addOverlay(helpTooltip);
        }


        /**
         * Creates a new measure tooltip
         */
        function createMeasureTooltip() {
          if (measureTooltipElement) {
            measureTooltipElement.parentNode.removeChild(measureTooltipElement);
          }
          measureTooltipElement = document.createElement('div');
          measureTooltipElement.className = 'tooltip tooltip-measure';
          measureTooltip = new Overlay({
            element: measureTooltipElement,
            offset: [0, -15],
            positioning: 'bottom-center'
          });
          map.addOverlay(measureTooltip);
        }


        /**
         * Let user change the geometry type.
         */
        typeSelectmea.onchange = function () {
          map.removeInteraction(drawmea);
          addInteractionmea();
        };

        addInteractionmea();
        //  	//设置显示地图的中心点和级别 
        // // map.centerAndZoom(new T.LngLat(116.40969,39.89945),zoom); 
        // administrative = new T.AdministrativeDivision();
        //     var config = {
        //       needSubInfo: false,
        //       needAll: false,
        //       needPolygon: true,
        //       needPre: true,
        //       searchType: 1,
        //       searchWord: area
        //     };
        //     administrative.search(config, searchResult);
        //     function searchResult(result)
        //     {
        //       if(result.getStatus() == 100) 
        //       {
        //         var data = result.getData();
        //         showMsg(data);
        //         // document.getElementById("administrativeMsg").innerHTML = html; 
        //       }
        //       else 
        //       {
        //         result.getMsg();
        //       }
        //     }

        //     function showMsg(data){
        //       for(var i = 0; i < data.length; i++){
        //         //解释上级行政区划
        //         if(data[i].parents){
        //           var upLevel = "";
        //           if(data[i].parents.country){
        //             upLevel += data[i].parents.country.name;
        //           }
        //           if(data[i].parents.province){
        //             upLevel += data[i].parents.province.name;
        //           }
        //         }
        //         if(data[i].points){
        //           //绘制行政区划
        //           polygon(data[i].points);
        //           console.log(data[i].points);
        //         }
        //         //解释下级行政区划
        //         if(data[i].child){
        //           showMsg(data[i].child);
        //           if(data[i].child.points){
        //             //绘制行政区划
        //             polygon(data[i].child.points);
        //           }
        //         }
        //       }
        //     }

        //     function polygon(points){
        //       var pointsArr = [];
        //           for (var i = 0; i < points.length; i++) {
        //               var regionLngLats = [];
        //               var regionArr = points[i].region.split(",");
        //               for (var m = 0; m < regionArr.length; m++) {
        //                   var lnglatArr = regionArr[m].split(" ");
        //                   var lnglat = new T.LngLat(lnglatArr[0], lnglatArr[1]);
        //                   regionLngLats.push(lnglat);
        //                   pointsArr.push(lnglat);
        //               }
        //               //创建面对象
        //             var polygon = new T.Polygon(regionLngLats,{color: "red", weight: 3, opacity: 1, fillColor: "#00FF00", fillOpacity: 0.3});
        //             //向地图上添加行政区划面
        //             map.addOverLay(polygon);
        //           }
        //           //显示最佳比例尺
        //           map.setViewport(pointsArr);
        //     }

      },
      clear() {
        source.clear();
        sourcemea.clear();
        map.getOverlays().clear();
      },
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
    position: relative;
  }

  .map {
    height: 100%;
    width: 100%;
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

  .form-inline {
    position: absolute;
    top: 2%;
    left: 4%;
    z-index: 999999999999999999;
  }

  .form-inline_mea {
    position: absolute;
    top: 2%;
    left: 28%;
    z-index: 999999999999999999;
  }

  .map:-moz-full-screen {
    height: 100%;
  }

  .map:-webkit-full-screen {
    height: 100%;
  }

  .map:-ms-fullscreen {
    height: 100%;
  }

  .map:fullscreen {
    height: 100%;
  }

  .ol-rotate {
    top: 3em;
  }

  .clear {
    position: absolute;
    top: 2%;
    left: 68%;
    z-index: 999999999999999999;
  }
  .top10{
    position: absolute;
    top: 8%;
    left: 4%;
    z-index: 999999;
  }
  .top20{
    position: absolute;
    top: 8%;
    left: 24%;
    z-index: 999999;
  }
  hr{
    margin: 0;
  }
</style>
<style>
  #mouse-position {
      float: left;
      position: absolute;
      bottom: 5px;
      width: 200px;
      height: 20px;
      /* 将z-index设置为显示在地图上层 */
      z-index: 2000;
  }
  /* 显示鼠标信息的自定义样式设置 */

  .custom-mouse-position {
      color: red;
      font-size: 16px;
      font-family: "微软雅黑";
  }
  .tdt-control-copyright {
    display: none !important;
  }

  .tooltip {
    position: relative;
    background: rgba(0, 0, 0, 0.5);
    border-radius: 4px;
    color: white;
    padding: 4px 8px;
    opacity: 0.7;
    white-space: nowrap;
  }

  .tooltip-measure {
    opacity: 1;
    font-weight: bold;
  }

  .tooltip-static {
    background-color: #ffcc33;
    color: black;
    border: 1px solid white;
  }

  .tooltip-measure:before,
  .tooltip-static:before {
    border-top: 6px solid rgba(0, 0, 0, 0.5);
    border-right: 6px solid transparent;
    border-left: 6px solid transparent;
    content: "";
    position: absolute;
    bottom: -6px;
    margin-left: -7px;
    left: 50%;
  }

  .tooltip-static:before {
    border-top-color: #ffcc33;
  }

</style>
