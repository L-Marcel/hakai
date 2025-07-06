import { ReactNode, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import useRoom from "@stores/useRoom";
import { getOpenRoom } from "../../services/room";

interface Props {
  children: ReactNode;
}

export default function DashboardGuard({ children }: Props) {
  const [checked, setChecked] = useState(false);

  const room = useRoom((state) => state.room);
  const navigate = useNavigate();

  useEffect(() => {
    if (!room) {
      getOpenRoom()
        .then((room) => {
          navigate("/room/panel/" + room.code);
        })
        .catch(() => {
          setChecked(true);
        });
    }
  }, [room, navigate, setChecked]);

  return checked ? children : null;
}
