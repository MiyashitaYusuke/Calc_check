package jp.alhinc.miyashita_yusuke.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class Calc {

public static void main(String[] args) {

HashMap<String,String> branch = new HashMap<String,String>();
HashMap<String,String> commodity = new HashMap<String,String>();
HashMap<String,Long> branchTotal = new HashMap<String,Long>();
HashMap<String,Long> commodityTotal = new HashMap<String,Long>();
ArrayList<File> sale = new ArrayList<File>();


//branch.lstを読み込む

try {
	File file  = new File(args[0],"branch.lst");
	if (!file.exists()) {
	System.out.println("支店定義ファイルがみつかりません");
	return;
	}

	FileReader fr = new FileReader(file);
	BufferedReader br = new BufferedReader(fr);
	String a;
	while((a = br.readLine()) != null) {
		//配列branchSplitを用意し、キーと値を,で分ける。
		String[] branchSplit = a.split(",", 0);
		if (branchSplit.length ==2 && branchSplit[0].matches("^\\d{3}$")) {
			//	System.out.println(branchSplit[0] + "," + branchSplit[1]);
		} else {
			System.out.println("支店定義ファイルのフォーマットが不正です");
			break;
		}
		branch.put(branchSplit[0],branchSplit[1]);
		branchTotal.put(branchSplit[0],0L);
	}
	br.close();
	}
	catch(IOException e) {
		System.out.println("予期せぬエラーが発生しました");
	} catch(ArrayIndexOutOfBoundsException p) {
		System.out.println("予期せぬエラーが発生しました");
	}
	//branch.lstの読み込み終了

	//commodity.lstを読み込む
	try {
		File file  = new File(args[0],"commodity.lst");
		if (!file.exists()) {
			System.out.println("商品定義ファイルがみつかりません");
			return;
	}
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String b;
		while ((b = br.readLine()) != null) {
			String[] commoditySplit = b.split(",",0);
			if(commoditySplit.length ==2 && commoditySplit[0].matches("^\\w{8}$") ) {
				//	System.out.println(commoditySplit[0] + "," + commoditySplit[1]);
				} else {
					System.out.println("商品定義ファイルのフォーマットが不正です");
					break;
				}
			commodity.put(commoditySplit[0],commoditySplit[1]);
			commodityTotal.put(commoditySplit[0],0L);
		}
		br.close();
	} catch(IOException e) {
		System.out.println("予期せぬエラーが発生しました");
	}
	catch(ArrayIndexOutOfBoundsException p) {
		System.out.println("予期せぬエラーが発生しました");
	}

	//commodity.lstの読み込み終了
	try {
		String path = args[0];
		File dir = new File(path);
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			//getName()で、ファイル名だけ所得
			//("^\\d{8}$")8桁指定 ^が始まり、$が終わりを指定
			if (file.getName().matches("^\\d{8}.rcd$")) {
				sale.add(file);
				}
		}
	}
	catch(ArrayIndexOutOfBoundsException p) {
		System.out.println("予期せぬエラーが発生しました");
	}
	try {
	//連番処理
		Collections.sort(sale);
		String path2 = args[0];
		File dir2 = new File(path2);
		File[] files2 = dir2.listFiles();
		int sl = sale.size();
		Long coLo[] = new Long[sl];
		int add= 0;
		for (int i = 0; i<sale.size(); i++){
			add = i-1;
			String str = sale.get(i).getName();
			String coRep = str.replace(".rcd","");
			coLo[i] = Long.parseLong(coRep);
			if (i >=1) {
				if (coLo[i] - coLo[add] ==1 ) {
					//	System.out.println(sale.get(i).getName());
				} else {
					System.out.println("連番になってません");
					return;
				}
			}else {
				//	System.out.println(sale.get(i).getName());
			}
		}
	}
	catch (RuntimeException r) {
		System.out.println("予期せぬエラーが発生しました");
	}



	//集計
	try {
		for (int i = 0; i<sale.size(); i++) {
			ArrayList<String> totalList = new ArrayList<String>();
			FileReader fr = new FileReader(sale.get(i));
			BufferedReader br = new BufferedReader(fr);
			String x;
			while((x = br.readLine()) != null) {
				totalList.add(x);
			}
			br.close();
			if (branchTotal.get(totalList.get(0)) == null){
				System.out.println(sale.get(i).getName() +"の支店コードが不正です");
				return;
				}
			if (commodityTotal.get(totalList.get(1)) == null){
				System.out.println(sale.get(i).getName() + "の商品コードが不正です");
				return;
				}
			if (totalList.size() !=3) {
				System.out.println(sale.get(i).getName() +"のフォーマットが不正です");
				return;
			}
			long all = Long.parseLong(totalList.get(2));
			branchTotal.put(totalList.get(0),
					branchTotal.get(
							totalList.get(0))+all);
			commodityTotal.put(totalList.get(1),
					commodityTotal.get(
							totalList.get(1))+all);
			List<Map.Entry<String,Long>> sortBranch = new ArrayList<Map.Entry<String,Long>>(branchTotal.entrySet());
			Collections.sort(sortBranch, new Comparator<Map.Entry<String,Long>>() {
				public int compare(
						Entry<String,Long> entry1, Entry<String,Long> entry2) {
					return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
				}
			});

			List<Map.Entry<String,Long>> sortCommodity = new ArrayList<Map.Entry<String,Long>>(commodityTotal.entrySet());
			Collections.sort(sortCommodity, new Comparator<Map.Entry<String,Long>>() {
				public int compare(
						Entry<String,Long> entry1, Entry<String,Long> entry2) {
					return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
				}
			});
        	for (Entry<String,Long> s : sortBranch) {
        		if(s.getValue() >= (10000000000L)) {
        			System.out.println("合計金額が10桁を超えました");
        			return;
        		} else {
 //       			System.out.println(s.getKey() + "," + branch.get(s.getKey()) + "," + s.getValue());
        		}
        	}
        	for (Entry<String,Long> s: sortCommodity) {
        		if(s.getValue() >=10000000000L) {
        			System.out.println("合計金額が10桁を超えました");
    				return;
        		}else {
 //       			System.out.println(s.getKey() + "," + commodity.get(s.getKey()) +"," + s.getValue());
        		}
        	}
		}
	}
	catch(ArrayIndexOutOfBoundsException p) {
		System.out.println("予期せぬエラーが発生しました");
	}
	catch (IOException e) {
		System.out.println("予期せぬエラーが発生しました");
	}

	List<Map.Entry<String,Long>> sortBranch = new ArrayList<Map.Entry<String,Long>>(branchTotal.entrySet());
	Collections.sort(sortBranch, new Comparator<Map.Entry<String,Long>>() {
		public int compare(
				Entry<String,Long> entry1, Entry<String,Long> entry2) {
			return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
		}
	});

	List<Map.Entry<String,Long>> sortCommodity = new ArrayList<Map.Entry<String,Long>>(commodityTotal.entrySet());
	Collections.sort(sortCommodity, new Comparator<Map.Entry<String,Long>>() {
		public int compare(
				Entry<String,Long> entry1, Entry<String,Long> entry2) {
			return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
		}
	});

	        //ファイル出力
	        try {
    			File branchFile = new File(args[0],"branch.out");
            	branchFile.createNewFile();
	        	File file = new File(args[0],"branch.out");
	        	FileWriter fw = new FileWriter(file);
	        	BufferedWriter bw = new BufferedWriter(fw);
	        	for (Entry<String,Long> s : sortBranch) {
	        		bw.write(s.getKey() + "," + branch.get(s.getKey()) + "," + s.getValue() + "\r\n");
	        	}
	        	bw.close();
	        }
	        catch (IOException e) {
	        	System.out.println("予期せぬエラーが発生しました");
	        }
	        catch(ArrayIndexOutOfBoundsException p) {
	        	System.out.println("予期せぬエラーが発生しました");
	        	}

	        try {
    			File commodityFile = new File(args[0],"commodity.out");
	        	commodityFile.createNewFile();
	        	File file = new File(args[0],"commodity.out");
	        	FileWriter fw = new FileWriter(file);
	        	BufferedWriter bw = new BufferedWriter(fw);
	        	for (Entry<String,Long> s : sortCommodity) {
	        		bw.write(s.getKey() + "," + commodity.get(s.getKey()) +"," + s.getValue() + "\r\n");
	        	}
	        	bw.close();
	        }
	        catch (IOException e) {
	        	System.out.println("予期せぬエラーが発生しました");
	        }
	        catch(ArrayIndexOutOfBoundsException p) {
	        	System.out.println("予期せぬエラーが発生しました");
	        }
	        //ファイル出力終了
	}
}