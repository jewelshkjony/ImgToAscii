# Add any ProGuard configurations specific to this
# extension here.

-keep public class com.jewel.imgtoascii.ImgToAscii {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 5
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'com/jewel/imgtoascii/repack'
-flattenpackagehierarchy
-dontpreverify
