package com.edu.kygroup.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import com.edu.kygroup.R;

public class Util {
	public static List<int[]> objectList = new ArrayList<int[]>();
	public static List<int[]> objec_code_tList = new ArrayList<int[]>();

	public static int citySchoolArray[] = { R.array.beijingD, R.array.tianjinD,
			R.array.shanhaiD, R.array.chongqingD, R.array.hebeiD,
			R.array.neimengguD, R.array.liaoningD, R.array.jilinD,
			R.array.heilongjiangD, R.array.jiangsuD, R.array.zhejiangD,
			R.array.anhuiD, R.array.fujianD, R.array.jiangxiD,
			R.array.shandongD, R.array.henanD, R.array.hubeiD, R.array.hunanD,
			R.array.guangdongD, R.array.guagnxiD, R.array.hainanD,
			R.array.sichuanD, R.array.guizhouD, R.array.yunnanD,
			R.array.xizangD, R.array.shanxiD, R.array.gansuD, R.array.qinghaiD,
			R.array.ningxiaD, R.array.xinjiangD };
	public static int citySchoolCodeArray[] = { R.array.beijing,
			R.array.tianjin, R.array.shanghai, R.array.chongqin, R.array.hebei,
			R.array.neimenggu, R.array.liaoning, R.array.jilin,
			R.array.heilongjiang, R.array.jiangsu, R.array.zhejiang,
			R.array.anhui, R.array.fujian, R.array.jiangxi, R.array.shandong,
			R.array.henan, R.array.hubei, R.array.hunan, R.array.guangdong,
			R.array.guangxi, R.array.hainan, R.array.sichuan, R.array.guizhou,
			R.array.yunnan, R.array.xizang, R.array.shanxi, R.array.gansu,
			R.array.qingha, R.array.ningxia, R.array.xinjiang };

	public static int unitArray[] = { R.array.jiaoyubu,
			R.array.zhonggongzhongyangbangongting, R.array.waijiaobu,
			R.array.zhongguokexueyuan, R.array.guojiaminzushiwuweiyuanhui,
			R.array.gonganbu, R.array.sifabu, R.array.gongyeyuxinxihuabu,
			R.array.guojiaweishenghejihuashegnyuweiyuanhui,
			R.array.haiguanzongshu, R.array.zhongguomingyonghangkongju,
			R.array.zhongguodizhenju, R.array.guowuyuanqiaowubangongshi,
			R.array.guojiaanquanshengchanjianduguanliju,
			R.array.guojiatiyuzongju, R.array.zhonghuaquanguozonggonghui,
			R.array.zhongguogongchanzhuyiqingniantuanzhongyang,
			R.array.zhonghuaquanguofunvlianhehui, R.array.beijingshi,
			R.array.tianjinshi, R.array.shanghaishi, R.array.hebeisheng,
			R.array.shanxisheng, R.array.neimengguzizhiqu,
			R.array.liaoningsheng, R.array.jilinsheng,
			R.array.heilongjiangsheng, R.array.jiangsusheng,
			R.array.zhejiangsheng, R.array.anhuisheng, R.array.fujiansheng,
			R.array.jiangxisheng, R.array.shandongsheng, R.array.henansheng,
			R.array.hubeisheng, R.array.hunansheng, R.array.guangdongsheng,
			R.array.guangxizhuangzuzizhiqu, R.array.hainansheng,
			R.array.chongqingshi, R.array.sichuansheng, R.array.guizhousheng,
			R.array.yunnansheng, R.array.xizangzizhiqu, R.array.sanxisheng,
			R.array.gansusheng, R.array.qinghaisheng,
			R.array.ningxiahuizuzizhiqu, R.array.xinjiangweiwuerzizhiqu,
			R.array.kexuejishubu, R.array.caizhegnbu, R.array.shagnwubu,
			R.array.nognyebu, R.array.shuilibu,
			R.array.zhufanghechengxiangjianshebu, R.array.guotuziyuanbu,
			R.array.xinjiangshengchanjianshebingtuan,
			R.array.zhongguotieluzonggongsi, R.array.jiaotongyunshubu,
			R.array.wenhuabu, R.array.guojiaguagnbodianyingdianshizongju,
			R.array.zhongguoqixiangju, R.array.guojiahaiyangju,
			R.array.guojiazhiliangjiandujianyanjianyizongju,
			R.array.guojialinyeju, R.array.huanjingbaohubu,
			R.array.guojiayancaozhuanmaiqu,
			R.array.guojiashipinyaopinjianduguanlizongju,
			R.array.guojiacehuiju, R.array.guojiazhognyiyaoguanliju,
			R.array.guowuyuanbangognting,
			R.array.zhongguogongchegnwuliyanjiuyuan,
			R.array.zhongguoshehuikexueyuan,
			R.array.zhongguoshiyouhuagognjituangongsi,
			R.array.zhongguoshiyoutianranqijituangongsi,
			R.array.guojiadianwanggongsi,
			R.array.zhongguodianzikejijituangongsi,
			R.array.zhongguodianzixinxichanyejituanyouxiangongsi,
			R.array.datangdianxinkejichanyejituan,
			R.array.wuhanyoudiankexueyanjiuyuan,
			R.array.zhongguoyiyaojituanzonggongsi,
			R.array.zhongguozhonggangjituangongsi,
			R.array.zhongguomeitankegongjituan, R.array.jixiekexueyanjiuyuan,
			R.array.dangxiao, R.array.jiefangjun,
			R.array.zhongguohegongyejituangongsi,
			R.array.guojiahedianjishuyouxiangongsi,
			R.array.zhongguochuanbozhonggongjituan,
			R.array.zhongguochuanbogongyejituan, R.array.zhongguohuanengjituan,
			R.array.zhongguojianzhukexueyanjiuyuan,
			R.array.zhongguojianshekejijituan,
			R.array.zhongguoyejinkegongjituan,
			R.array.zhongguogangyankejijituan,
			R.array.zhongguojixiegongyejituan, R.array.zhongguozhonghuajituan,
			R.array.zhongguoqinggongjituan,
			R.array.zhongguohangtiankejijituangongsi,
			R.array.zhongguobingqizhuangbeijituangongsi,
			R.array.zhongguobingqigongyejituangongsi,
			R.array.zhongguohangtiankejijituangongsi,
			R.array.zhongguohangtiankegongjituan,
			R.array.zhongguojiancaijituan, R.array.zhongguowukuangjituan,
			R.array.beijingyousejinshuyanjiuzongyuan,
			R.array.zhongguoyousekuangyejituan };
	public static int unitCodeArray[] = { R.array.jiaoyubu_code,
			R.array.zhonggongzhongyangbangongting_code, R.array.waijiaobu_code,
			R.array.zhongguokexueyuan_code,
			R.array.guojiaminzushiwuweiyuanhui_code, R.array.gonganbu_code,
			R.array.sifabu_code, R.array.gongyeyuxinxihuabu_code,
			R.array.guojiaweishenghejihuashengyuweiyuanhui_code,
			R.array.haiguanzongshu_code,
			R.array.zhongguominyonghangkongju_code,
			R.array.zhongguodizhenju_code,
			R.array.guowuyuanqiaowubangongshi_code,
			R.array.guojiaanquanshengchanjianduguanlizongju_code,
			R.array.guojiatiyuzongju_code,
			R.array.zhonghuaquanguozonggonghui_code,
			R.array.zhongguogongchanzhuyiqingniantuanzhongyang_code,
			R.array.zhonghuaquanguofunvlianhehui_code, R.array.beijingshi_code,
			R.array.tianjinshi_code, R.array.shanghaishi_code,
			R.array.hebeisheng_code, R.array.shanxisheng_code,
			R.array.neimengguzizhiqu_code, R.array.liaoningsheng_code,
			R.array.jilinsheng_code, R.array.heilongjiangsheng_code,
			R.array.jiangsusheng_code, R.array.zhejiangsheng_code,
			R.array.anhuisheng_code, R.array.fujiansheng_code,
			R.array.jiangxisheng_code, R.array.shandongsheng_code,
			R.array.henansheng_code, R.array.hubeisheng_code,
			R.array.hunansheng_code, R.array.guangdongsheng_code,
			R.array.guangxizhuangzuzizhiqu_code, R.array.hainansheng_code,
			R.array.chongqingshi_code, R.array.sichuansheng_code,
			R.array.guizhousheng_code, R.array.yunnansheng_code,
			R.array.xicangzizhiqu_code, R.array.sanxisheng_code,
			R.array.gansusheng_code, R.array.qinghaisheng_code,
			R.array.ningxiahuizuzizhiqu_code,
			R.array.xinjiangweiwuerzizhiqu_code, R.array.kexuejishubu_code,
			R.array.caizhengbu_code, R.array.shangwubu_code,
			R.array.nongyebu_code, R.array.shuilibu_code,
			R.array.zhufanghechengxiangjianshebu_code,
			R.array.guotuziyuanbu_code,
			R.array.xinjiangshengchanjianshebingtuan_code,
			R.array.zhongguotieluzonggongsi_code,
			R.array.jiaotongyunshubu_code, R.array.wenhuabu_code,
			R.array.guojiaguangbodianyingdianshizongju_code,
			R.array.zhongguoqixiangju_code, R.array.guojiahaiyangju_code,
			R.array.guojiazhiliangjiandujianyanjianyizongju_code,
			R.array.guojialinyeju_code, R.array.huanjingbaohubu_code,
			R.array.guojiayancaozhuanmaiju_code,
			R.array.guojiashipinyaopinjianduguanlizongju_code,
			R.array.guojiacehuiju_code, R.array.guojiazhongyiyaoguanliju_code,
			R.array.guowuyuanbangongting_code,
			R.array.zhongguogongchengwuliyanjiuyuan_code,
			R.array.zhongguoshehuikexueyuan_code,
			R.array.zhongguoshiyouhuagongjituangongsi_code,
			R.array.zhongguoshiyoutianranqijituangongsi_code,
			R.array.guojiadianwanggongsi_code,
			R.array.zhongguodianzikejijituangongsi_code,
			R.array.zhongguodianzixinxichanyejituanyouxiangongsi_code,
			R.array.datangdianxinkejichanyejituan_code,
			R.array.wuhanyoudiankexueyanjiuyuan_code,
			R.array.zhongguoyiyaojituanzonggongsi_code,
			R.array.zhongguozhonggangjituangongsi_code,
			R.array.zhongguomeitankegongjituan_code,
			R.array.jixiekexueyanjiuyuan_code, R.array.dangxiao_code,
			R.array.jiefangjun_code, R.array.zhongguohegongyejituangongsi_code,
			R.array.guojiahedianjishuyouxiangongsi_code,
			R.array.zhongguochuanbozhonggongjituan_code,
			R.array.zhongguochuanbogongyejituan_code,
			R.array.zhongguohuanengjituan_code,
			R.array.zhongguojianzhukexueyanjiuyuan_code,
			R.array.zhongguojianshekejijituan_code,
			R.array.zhongguoyejinkegongjituan_code,
			R.array.zhongguogangyankejijituan_code,
			R.array.zhongguojixiegongyejituan_code,
			R.array.zhongguozhonghuajituan_code,
			R.array.zhongguoqinggongjituan_code,
			R.array.zhongguohangtiankejijituangongsi_code,
			R.array.zhongguobingqizhuangbeijituangongsi_code,
			R.array.zhongguobingqigongyejituangongsi_code,
			R.array.zhongguohangtiankejijituangongsi_code,
			R.array.zhongguohangtiankegongjituan_code,
			R.array.zhongguojiancaijituan_code,
			R.array.zhongguowukuangjituan_code,
			R.array.beijingyousejinshuyanjiuzongyuan_code,
			R.array.zhongguoyousekuangyejituan };

	public static int subjectArray[] = { R.array.name_1, R.array.name_2,
			R.array.name_3, R.array.name_4, R.array.name_5, R.array.name_6,
			R.array.name_7, R.array.name_8, R.array.name_9, R.array.name_10,
			R.array.name_11, R.array.name_12, R.array.name_13, R.array.name_14 };
	public static int subjectCodeArray[] = { R.array.name_1_code,
			R.array.name_2_code, R.array.name_3_code, R.array.name_4_code,
			R.array.name_5_code, R.array.name_6_code, R.array.name_7_code,
			R.array.name_8_code, R.array.name_9_code, R.array.name_10_code,
			R.array.name_11_code, R.array.name_12_code, R.array.name_14_code,
			R.array.name_13_code };
	public static int name_1_Array[] = { R.array.name_101 };
	public static int name_2_Array[] = { R.array.name_201, R.array.name_202,
			R.array.name_251, R.array.name_252, R.array.name_253,
			R.array.name_254, R.array.name_255, R.array.name_256,
			R.array.name_257, R.array.name_270 };
	public static int name_3_Array[] = { R.array.name_301, R.array.name_302,
			R.array.name_303, R.array.name_304, R.array.name_305,
			R.array.name_306 };
	public static int name_4_Array[] = { R.array.name_401, R.array.name_402,
			R.array.name_403, R.array.name_471 };
	public static int name_5_Array[] = { R.array.name_501, R.array.name_502,
			R.array.name_503, R.array.name_504 };
	public static int name_6_Array[] = { R.array.name_601, R.array.name_602,
			R.array.name_603, };
	public static int name_7_Array[] = { R.array.name_701, R.array.name_702,
			R.array.name_703, R.array.name_704, R.array.name_705,
			R.array.name_706, R.array.name_707, R.array.name_708,
			R.array.name_709, R.array.name_710, R.array.name_711,
			R.array.name_712, R.array.name_713, R.array.name_714,
			R.array.name_771, R.array.name_772, R.array.name_773,
			R.array.name_774, R.array.name_775, R.array.name_776,
			R.array.name_777, R.array.name_778, R.array.name_779,
			R.array.name_780, R.array.name_781, R.array.name_784,
			R.array.name_785, R.array.name_786, };
	public static int name_8_Array[] = { R.array.name_801, R.array.name_802,
			R.array.name_803, R.array.name_804, R.array.name_805,
			R.array.name_806, R.array.name_807, R.array.name_808,
			R.array.name_809, R.array.name_810, R.array.name_811,
			R.array.name_812, R.array.name_813, R.array.name_814,
			R.array.name_815, R.array.name_816, R.array.name_817,
			R.array.name_818, R.array.name_819, R.array.name_820,
			R.array.name_821, R.array.name_822, R.array.name_823,
			R.array.name_824, R.array.name_825, R.array.name_826,
			R.array.name_827, R.array.name_828, R.array.name_829,
			R.array.name_830, R.array.name_831, R.array.name_832,
			R.array.name_833, R.array.name_834, R.array.name_835,
			R.array.name_837, R.array.name_838, R.array.name_870,
			R.array.name_871, R.array.name_872, };
	public static int name_9_Array[] = { R.array.name_901, R.array.name_902,
			R.array.name_903, R.array.name_904, R.array.name_905,
			R.array.name_906, R.array.name_907, R.array.name_908,
			R.array.name_909, R.array.name_971, R.array.name_972,
			R.array.name_973, };
	public static int name_10_Array[] = { R.array.name_1001, R.array.name_1002,
			R.array.name_1003, R.array.name_1004, R.array.name_1005,
			R.array.name_1006, R.array.name_1007, R.array.name_1008,
			R.array.name_1009, R.array.name_1011, R.array.name_1072,
			R.array.name_1073, };
	public static int name_11_Array[] = { R.array.name_1101, R.array.name_1102,
			R.array.name_1103, R.array.name_1104, R.array.name_1105,
			R.array.name_1106, R.array.name_1107, R.array.name_1108,
			R.array.name_1109, R.array.name_1110, };
	public static int name_12_Array[] = { R.array.name_1201, R.array.name_1202,
			R.array.name_1203, R.array.name_1204, R.array.name_1205, };
	public static int name_13_Array[] = { R.array.name_1301, R.array.name_1302,
			R.array.name_1303, R.array.name_1304, R.array.name_1305, };
	public static int name_14_Array[] = { R.array.name_251, R.array.name_252,
			R.array.name_253, R.array.name_254, R.array.name_255,
			R.array.name_256, R.array.name_257, R.array.name_351,
			R.array.name_352, R.array.name_353, R.array.name_451,
			R.array.name_452, R.array.name_453, R.array.name_454,
			R.array.name_551, R.array.name_552, R.array.name_553,
			R.array.name_651, R.array.name_851, R.array.name_852,
			R.array.name_853, R.array.name_951, R.array.name_952,
			R.array.name_953, R.array.name_954, R.array.name_1051,
			R.array.name_1052, R.array.name_1053, R.array.name_1054,
			R.array.name_1055, R.array.name_1056, R.array.name_1151,
			R.array.name_1251, R.array.name_1252, R.array.name_1253,
			R.array.name_1254, R.array.name_1255, R.array.name_1255,
			R.array.name_1351, };

	public static int name_code_1_Array[] = { R.array.name_101_code };
	public static int name_code_2_Array[] = { R.array.name_201_code,
			R.array.name_202_code, R.array.name_251_code,
			R.array.name_252_code, R.array.name_253_code,
			R.array.name_254_code, R.array.name_255_code,
			R.array.name_256_code, R.array.name_257_code, R.array.name_270 };
	public static int name_code_3_Array[] = { R.array.name_301_code,
			R.array.name_302_code, R.array.name_303_code,
			R.array.name_304_code, R.array.name_305_code, R.array.name_306 };
	public static int name_code_4_Array[] = { R.array.name_401_code,
			R.array.name_402_code, R.array.name_403_code, R.array.name_471 };
	public static int name_code_5_Array[] = { R.array.name_501_code,
			R.array.name_502_code, R.array.name_503_code, R.array.name_504 };
	public static int name_code_6_Array[] = { R.array.name_601_code,
			R.array.name_602_code, R.array.name_603_code, };
	public static int name_code_7_Array[] = { R.array.name_701_code,
			R.array.name_702_code, R.array.name_703_code,
			R.array.name_704_code, R.array.name_705_code,
			R.array.name_706_code, R.array.name_707_code,
			R.array.name_708_code, R.array.name_709_code,
			R.array.name_710_code, R.array.name_711_code,
			R.array.name_712_code, R.array.name_713_code,
			R.array.name_714_code, R.array.name_771_code,
			R.array.name_772_code, R.array.name_773_code,
			R.array.name_774_code, R.array.name_775_code,
			R.array.name_776_code, R.array.name_777_code,
			R.array.name_778_code, R.array.name_779_code,
			R.array.name_780_code, R.array.name_781_code,
			R.array.name_784_code, R.array.name_785_code,
			R.array.name_786_code, };
	public static int name_code_8_Array[] = { R.array.name_801_code,
			R.array.name_802_code, R.array.name_803_code,
			R.array.name_804_code, R.array.name_805_code,
			R.array.name_806_code, R.array.name_807_code,
			R.array.name_808_code, R.array.name_809_code,
			R.array.name_810_code, R.array.name_811_code,
			R.array.name_812_code, R.array.name_813_code,
			R.array.name_814_code, R.array.name_815_code,
			R.array.name_816_code, R.array.name_817_code,
			R.array.name_818_code, R.array.name_819_code,
			R.array.name_820_code, R.array.name_821_code,
			R.array.name_822_code, R.array.name_823_code,
			R.array.name_824_code, R.array.name_825_code,
			R.array.name_826_code, R.array.name_827_code,
			R.array.name_828_code, R.array.name_829_code,
			R.array.name_830_code, R.array.name_831_code,
			R.array.name_832_code, R.array.name_833_code,
			R.array.name_834_code, R.array.name_835_code,
			R.array.name_837_code, R.array.name_838_code,
			R.array.name_870_code, R.array.name_871_code,
			R.array.name_872_code, };
	public static int name_code_9_Array[] = { R.array.name_901_code,
			R.array.name_902_code, R.array.name_903_code,
			R.array.name_904_code, R.array.name_905_code,
			R.array.name_906_code, R.array.name_907_code,
			R.array.name_908_code, R.array.name_909_code,
			R.array.name_971_code, R.array.name_972_code,
			R.array.name_973_code, };
	public static int name_code_10_Array[] = { R.array.name_1001_code,
			R.array.name_1002_code, R.array.name_1003_code,
			R.array.name_1004_code, R.array.name_1005_code,
			R.array.name_1006_code, R.array.name_1007_code,
			R.array.name_1008_code, R.array.name_1009_code,
			R.array.name_1011_code, R.array.name_1072_code,
			R.array.name_1073_code, };
	public static int name_code_11_Array[] = { R.array.name_1101_code,
			R.array.name_1102_code, R.array.name_1103_code,
			R.array.name_1104_code, R.array.name_1105_code,
			R.array.name_1106_code, R.array.name_1107_code,
			R.array.name_1108_code, R.array.name_1109_code,
			R.array.name_1110_code, };
	public static int name_code_12_Array[] = { R.array.name_1201_code,
			R.array.name_1202_code, R.array.name_1203_code,
			R.array.name_1204_code, R.array.name_1205_code, };
	public static int name_code_13_Array[] = { R.array.name_1301_code,
			R.array.name_1302_code, R.array.name_1303_code,
			R.array.name_1304_code, R.array.name_1305_code, };
	public static int name_code_14_Array[] = { R.array.name_251_code,
			R.array.name_252_code, R.array.name_253_code,
			R.array.name_254_code, R.array.name_255_code,
			R.array.name_256_code, R.array.name_257_code,
			R.array.name_351_code, R.array.name_352_code,
			R.array.name_353_code, R.array.name_451_code,
			R.array.name_452_code, R.array.name_453_code,
			R.array.name_454_code, R.array.name_551_code,
			R.array.name_552_code, R.array.name_553_code,
			R.array.name_651_code, R.array.name_851_code,
			R.array.name_852_code, R.array.name_853_code,
			R.array.name_951_code, R.array.name_952_code,
			R.array.name_953_code, R.array.name_954_code,
			R.array.name_1051_code, R.array.name_1052_code,
			R.array.name_1053_code, R.array.name_1054_code,
			R.array.name_1055_code, R.array.name_1056_code,
			R.array.name_1151_code, R.array.name_1251_code,
			R.array.name_1252_code, R.array.name_1253_code,
			R.array.name_1254_code, R.array.name_1255_code,
			R.array.name_1255_code, R.array.name_1351_code, };
	static {
		objectList.add(name_1_Array);
		objectList.add(name_2_Array);
		objectList.add(name_3_Array);
		objectList.add(name_4_Array);
		objectList.add(name_5_Array);
		objectList.add(name_6_Array);
		objectList.add(name_7_Array);
		objectList.add(name_8_Array);
		objectList.add(name_9_Array);
		objectList.add(name_10_Array);
		objectList.add(name_11_Array);
		objectList.add(name_12_Array);
		objectList.add(name_13_Array);
		objectList.add(name_14_Array);

		objec_code_tList.add(name_code_1_Array);
		objec_code_tList.add(name_code_2_Array);
		objec_code_tList.add(name_code_3_Array);
		objec_code_tList.add(name_code_4_Array);
		objec_code_tList.add(name_code_5_Array);
		objec_code_tList.add(name_code_6_Array);
		objec_code_tList.add(name_code_7_Array);
		objec_code_tList.add(name_code_8_Array);
		objec_code_tList.add(name_code_9_Array);
		objec_code_tList.add(name_code_10_Array);
		objec_code_tList.add(name_code_11_Array);
		objec_code_tList.add(name_code_12_Array);
		objec_code_tList.add(name_code_13_Array);
		objec_code_tList.add(name_code_14_Array);

	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getSecreenWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		return screenWidth;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getSecreenHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		int screenHeight = dm.heightPixels;
		return screenHeight;
	}

	/**
	 * 手机号码验证
	 * 
	 * @param
	 * @return
	 */
	public static boolean isPhoneNum(String strPhoneNum) {

		return strPhoneNum.startsWith("1") && strPhoneNum.length() == 11;
	}

	// 设置切换动画，从右边进入，左边退出
	public static void leftOutRightIn(Context context) {
		((Activity) context).overridePendingTransition(R.anim.push_left_in,
				R.anim.push_left_out);
	}

	// /**
	// * 右侧退出
	// *
	// * @param context
	// */
	// public static void rightOut(Context context) {
	// ((Activity) context).overridePendingTransition(R.anim.right_in,
	// R.anim.right_out);
	//
	// }
	public static String getCurrentDate() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return df.format(new Date());
	}
}
