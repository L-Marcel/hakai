package app.hakai.backend.dto;

import java.util.List;

import org.kahai.framework.dtos.request.CreateQuestionRequestBody;

public class CreateGameRequestBody {
  private String title;
    
  public CreateGameRequestBody() {
  };

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
    private List<CreateQuestionRequestBody> questions;


  public List<CreateQuestionRequestBody> getQuestions() {
    return this.questions;
  }

  public void setQuestions(List<CreateQuestionRequestBody> questions) {
    this.questions = questions;
  }
}
