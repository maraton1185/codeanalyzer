package codesnippetapp.data;

import java.util.ArrayList;

import javax.inject.Singleton;

//import javax.inject.Singleton;
//
@Singleton
//@Creatable
public class SnippetRepository {
	public ArrayList<SnippetData> snippets = new ArrayList<>();
	public String repositoryPath;
}
