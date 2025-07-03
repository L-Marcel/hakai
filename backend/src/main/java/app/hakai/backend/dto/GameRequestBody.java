package app.hakai.backend.dto;

import java.util.List;

import org.kahai.framework.dtos.request.QuestionRequestBody;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameRequestBody {
  private String title;
  private List<QuestionRequestBody> questions;
};
