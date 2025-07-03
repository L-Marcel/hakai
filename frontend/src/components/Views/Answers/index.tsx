import { AnswersHistory } from "@stores/useGame";

type GroupedByQuestion = {
  [questionId: string]: {
    question: string;
    answers: AnswersHistory[];
    groupedByNickname: {
      [nickname: string]: AnswersHistory[];
    };
  };
};

export const groupAnswers = (
  answers?: AnswersHistory[]
): GroupedByQuestion => {
  const grouped: GroupedByQuestion = {};

  if (answers?.length === 0) return {};

  answers?.forEach((answer) => {
    if (!grouped[answer.question]) {
      grouped[answer.question] = {
        question: answer.question, // nome de questão
        answers: [],
        groupedByNickname: {},
      };
    }
    grouped[answer.question].answers.push(answer);

    
    if (!grouped[answer.question].groupedByNickname[answer.nickname]) {
      grouped[answer.question].groupedByNickname[answer.nickname] = [];
    }
    
    grouped[answer.question].groupedByNickname[answer.nickname].push(answer);
  });

  return grouped;
};