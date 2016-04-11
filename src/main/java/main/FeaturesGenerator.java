package main;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import featureHelpers.*;

public class FeaturesGenerator {

	// GENERATE DOC FILES
	public static void generateDocFiles(File parentFolder, String folderName) throws Exception {
		Globals.DocFiles.put(folderName, getDocFiles(new File(parentFolder, folderName)));
	}

	private static List<File> getDocFiles(File docsDir) {
		
		List<File> result = new ArrayList<File>();
		
		for(File file : docsDir.listFiles()) {
			result.add(file);
		}
		
		return result;
	}
	
	// GENERATE FEATURES SIMILARITY
	public static void generateFeaturesSimilarities(File parentFolder, String folderName) throws Exception {
		setFeaturesSimilarities(folderName);
		
		normalizeFeaturesSimilarities();
	}

	private static void setFeaturesSimilarities(String folderName) throws Exception {
		List<File> docFiles = Globals.DocFiles.get(folderName);
		for (Feature feature : Globals.Features) {
		    List<DocumentsSimilarity> similarities = new ArrayList<DocumentsSimilarity>();
		    for(int i = 0; i < docFiles.size()-1;i++) {
		    	File file1 = docFiles.get(i);
		    	for(int j = i+1; j < docFiles.size();j++) {
			    	File file2 = docFiles.get(j);
			    	
			    	Document doc1 = new Document(file1);
			    	Document doc2 = new Document(file2);
			    	
			    	DocumentsSimilarity similarity = new DocumentsSimilarity();
			    	similarity.setDocument1(doc1.getFileName());
			    	similarity.setDocument2(doc2.getFileName());
			    	similarity.setScore(feature.getSimilarity(
		    				doc1, doc2
		    			));
			    	
			    	similarities.add(similarity);
			    }
		    }

	    	feature.setSimilarities(folderName, similarities);
		}
	}

	private static void normalizeFeaturesSimilarities() {
		for (Feature feature : Globals.Features) {
			feature.normalizeSimilarities();
		}
	}
	
	// ACTUAL SIMILARITIES
	public static void setActualSimilarities(String folderName) throws Exception {
		File truthFile = new File(Config.truthFolderPath + "/" + folderName, "ranking.json");
		
		Type listType = new TypeToken<ArrayList<DocumentsSimilarity>>() {}.getType();
		List<DocumentsSimilarity> result = new Gson().fromJson(FileUtils.readFileToString(truthFile), listType);

		Globals.TrainSimilarities.put(folderName, result);
		
		FolderEvaluationData evaluations = new FolderEvaluationData();
		evaluations.trainEvaluatedCouplesCount = result.size();
		evaluations.totalDocCouplesCount = Utils.calculateCouplesCountFromTotal(Globals.DocFiles.get(folderName).size());
		
		evaluations.multiplyNumberForDocument = evaluations.totalDocCouplesCount / evaluations.trainEvaluatedCouplesCount; 

		Globals.FolderEvaluations.put(folderName, evaluations);
	}
}
