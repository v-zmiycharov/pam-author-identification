package features.core;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.wordnet.AnalyzerUtil;

import features.helpers.DocumentFeaturesHelpers;
import nlp.stopwords.StopWordItem;
import nlp.stopwords.StopWords;

public class IdentificationDocument {
	// COMMON PROPERTIES
	private String content;
	private String fullPath;
	private String fileName;
	private String folderName;
	private String language;
	private String genre;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public IdentificationDocument() {}
	
	public IdentificationDocument(File file, String language, String genre) throws Exception {
		this.content = FileUtils.readFileToString(file);
		this.fullPath = file.getAbsolutePath();
		this.fileName = file.getName();
		this.folderName = file.getParentFile().getName();
		
		this.language = language;
		this.genre = genre;
		
		setProperties();
	}
	
	// PROPERTIES
	private double meanSentenceLength;
	private Map<String, StopWordItem> stopWordsMap;
	private double[] punctuationMarksPercentages;

	public double getMeanSentenceLength() {
		return meanSentenceLength;
	}
	
	private void setMeanSentenceLength() {
		String[] sentences = AnalyzerUtil.getSentences(this.getContent(), 0);
		this.meanSentenceLength = DocumentFeaturesHelpers.getMeanSentenceLength(sentences);
	}

	public Map<String, StopWordItem> getStopWordsMap() {
		return stopWordsMap;
	}

	private void setStopWordsMap() throws Exception {
		StopWords sw = new StopWords(this.getLanguage());
		this.stopWordsMap = sw.count(this.content);
	}

	public double[] getPunctuationMarksPercentages() {
		return punctuationMarksPercentages;
	}

	public void setPunctuationMarksPercentages() throws Exception {
		this.punctuationMarksPercentages = DocumentFeaturesHelpers.getPunctuationMarksPercentages(this.getContent(), this.getLanguage());
	}

	private void setProperties() throws Exception {
		setMeanSentenceLength();
		setStopWordsMap();
		setPunctuationMarksPercentages();
	}

}
