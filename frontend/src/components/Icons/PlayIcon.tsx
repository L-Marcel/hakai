import { SVGProps } from "react";

export default function PlayIcon(props: SVGProps<SVGSVGElement>) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 0 14 14"
      width="1em"
      height="1em"
      {...props}
    >
      <path
        fill="currentColor"
        fillRule="evenodd"
        d="M2.676.02a1.74 1.74 0 0 0-.845.218a1.64 1.64 0 0 0-.895 1.433v10.677a1.64 1.64 0 0 0 .895 1.433a1.74 1.74 0 0 0 1.718-.016l8.63-5.338a1.61 1.61 0 0 0-.001-2.876L3.548.253A1.74 1.74 0 0 0 2.676.02"
        clipRule="evenodd"
      ></path>
    </svg>
  );
}
