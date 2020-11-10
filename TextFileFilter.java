package com.lucene.fuzzy;
import java.io.File;
import java.io.FileFilter;
public class TextFileFilter implements FileFilter {
 @Override
 public boolean accept(File pathname) {
 return pathname.getName().toLowerCase().endsWith(".txt"); //Only .txt files are interesting
 }
}

