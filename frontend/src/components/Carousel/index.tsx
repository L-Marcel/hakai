import { ReactNode, useEffect, useState } from "react";
import styles from "./index.module.scss";
import { GoDot, GoDotFill } from "react-icons/go";

interface Props<T> {
  items: T[];
  start: number | string;
  identifier: (item: T, index: number) => number | string;
  render: (item: T, key: number | string, index: number) => ReactNode;
}

export default function Carousel<T>({
  items,
  identifier,
  render,
  start,
}: Props<T>) {
  const [selected, setSelected] = useState(start);

  const onSelect = (item: T, index: number) => {
    setSelected(identifier(item, index));
  };

  useEffect(() => {
    setSelected(start);
  }, [start]);

  return (
    <div className={styles.carousel}>
      <div className={styles.switcher}>
        <ul>
          {items.map((item, index) => {
            if (selected === identifier(item, index))
              return (
                <li id="selected">
                  <button onClick={() => onSelect(item, index)}>
                    <GoDotFill />
                  </button>
                </li>
              );
            return (
              <li>
                <button onClick={() => onSelect(item, index)}>
                  <GoDot />
                </button>
              </li>
            );
          })}
        </ul>
      </div>
      <ul>
        {items.map((item, index) => {
          const key = identifier(item, index);
          if (selected === key) return render(item, key, index);
          else return null;
        })}
      </ul>
    </div>
  );
}
