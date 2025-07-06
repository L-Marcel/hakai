import { useNavigate } from "react-router-dom";
import useAuth from "@stores/useAuth";
import { ReactNode, useEffect } from "react";

interface Props {
  children: ReactNode;
  onlyUnauthenticated?: boolean;
}

export default function AuthGuard({
  children,
  onlyUnauthenticated = false,
}: Props) {
  const user = useAuth((state) => state.user);
  const navigate = useNavigate();

  useEffect(() => {
    if (!!user === onlyUnauthenticated)
      navigate(onlyUnauthenticated ? "/dashboard" : "/home");
  }, [user, onlyUnauthenticated, navigate]);

  return !!user !== onlyUnauthenticated ? children : null;
}
