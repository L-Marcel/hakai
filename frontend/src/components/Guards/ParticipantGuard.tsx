import { ReactNode } from "react";
import { useParams } from "react-router-dom";
import ParticipantPage from "@pages/Participant";

interface Props {
  children: ReactNode;
}

export default function ParticipantGuard({ children }: Props) {
  const { code } = useParams();

  // const check = useRoom((state) => state.check);
  // const navigate = useNavigate();

  // useEffect(() => {
  //   check(code).then((response: Result) => {
  //     if (response.ok) setExists(true);
  //     else navigate("/home");
  //   });
  // }, [code, check, navigate]);

  return code ? children : <ParticipantPage />;
}
