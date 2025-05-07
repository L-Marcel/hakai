import { ReactNode, useState } from "react";
import styles from "./index.module.scss";

interface Props<T> {
  items: T[];
  start: number | string;
  render: (selected: number | string, item: T, index: number) => ReactNode;
}

export default function Carousel<T>({ items, render, start }: Props<T>) {
  const [selected, setSelected] = useState(start);

  return (
    <ul className={styles.carousel}>
      {items.map((item, index) => {
        return render(selected, item, index);
      })}
    </ul>
  );
}
