import {
    toLonLat
  } from 'ol/proj';
var CommonFunc={};
CommonFunc.caculate={
    VincentyConstants : {
        a: 6378137,
        b: 6356752.3142,
        f: 1/298.257223563
    },
    /**
    *Calculate destination point given start point lat/long (numeric degrees),
    * bearing (numeric degrees) & distance (in m).
    */
    destinationVincenty : function(lonlat, brng, dist) {
        var u = this;
        var ct = u.VincentyConstants;
        var a = ct.a, b = ct.b, f = ct.f;

        var lon1 = lonlat[0];
        var lat1 = lonlat[1];

        var s = dist;
        var alpha1 = u.rad(brng);
        var sinAlpha1 = Math.sin(alpha1);
        var cosAlpha1 = Math.cos(alpha1);

        var tanU1 = (1-f) * Math.tan(u.rad(lat1));
        var cosU1 = 1 / Math.sqrt((1 + tanU1*tanU1)), sinU1 = tanU1*cosU1;
        var sigma1 = Math.atan2(tanU1, cosAlpha1);
        var sinAlpha = cosU1 * sinAlpha1;
        var cosSqAlpha = 1 - sinAlpha*sinAlpha;
        var uSq = cosSqAlpha * (a*a - b*b) / (b*b);
        var A = 1 + uSq/16384*(4096+uSq*(-768+uSq*(320-175*uSq)));
        var B = uSq/1024 * (256+uSq*(-128+uSq*(74-47*uSq)));

        var sigma = s / (b*A), sigmaP = 2*Math.PI;
        while (Math.abs(sigma-sigmaP) > 1e-12) {
            var cos2SigmaM = Math.cos(2*sigma1 + sigma);
            var sinSigma = Math.sin(sigma);
            var cosSigma = Math.cos(sigma);
            var deltaSigma = B*sinSigma*(cos2SigmaM+B/4*(cosSigma*(-1+2*cos2SigmaM*cos2SigmaM)-
                B/6*cos2SigmaM*(-3+4*sinSigma*sinSigma)*(-3+4*cos2SigmaM*cos2SigmaM)));
            sigmaP = sigma;
            sigma = s / (b*A) + deltaSigma;
        }

        var tmp = sinU1*sinSigma - cosU1*cosSigma*cosAlpha1;
        var lat2 = Math.atan2(sinU1*cosSigma + cosU1*sinSigma*cosAlpha1,
            (1-f)*Math.sqrt(sinAlpha*sinAlpha + tmp*tmp));
        var lambda = Math.atan2(sinSigma*sinAlpha1, cosU1*cosSigma - sinU1*sinSigma*cosAlpha1);
        var C = f/16*cosSqAlpha*(4+f*(4-3*cosSqAlpha));
        var L = lambda - (1-C) * f * sinAlpha *
            (sigma + C*sinSigma*(cos2SigmaM+C*cosSigma*(-1+2*cos2SigmaM*cos2SigmaM)));

        var revAz = Math.atan2(sinAlpha, -tmp);  // final bearing
        var lonlat=[lon1+u.deg(L), u.deg(lat2)];
        return lonlat;
    },

     /**
     * 度换成弧度
     * @param  {Float} d  度
     * @return {[Float}   弧度
     */
    rad:function(d)
    {
       return d * Math.PI / 180.0;
    },

    /**
     * 弧度换成度
     * @param  {Float} x 弧度
     * @return {Float}   度
     */
    deg:function(x) {
        return x*180/Math.PI;
    }   
}
export default CommonFunc