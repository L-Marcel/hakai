import { SVGProps } from "react";

export function EditIcon(props: SVGProps<SVGSVGElement>) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 0 26 26"
      width="1em"
      height="1em"
      {...props}
    >
      <path
        fill="currentColor"
        d="M20.094.25a2.25 2.25 0 0 0-1.625.656l-1 1.031l6.593 6.625l1-1.03a2.32 2.32 0 0 0 0-3.282L21.75.937A2.36 2.36 0 0 0 20.094.25m-3.75 2.594l-1.563 1.5l6.875 6.875L23.25 9.75zM13.78 5.438L2.97 16.155a.98.98 0 0 0-.5.625L.156 24.625a.975.975 0 0 0 1.219 1.219l7.844-2.313a.98.98 0 0 0 .781-.656l10.656-10.563l-1.468-1.468L8.25 21.813l-4.406 1.28l-.938-.937l1.344-4.593L15.094 6.75zm2.375 2.406l-10.968 11l1.593.343l.219 1.47l11-10.97z"
      ></path>
    </svg>
  );
}
