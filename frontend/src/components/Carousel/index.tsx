import { Fragment, ReactNode, useState } from "react";
import styles from "./index.module.scss";
import { GoDot, GoDotFill } from "react-icons/go";

interface Props<T> {
  items: T[];
  start: number | string;
  identifier: (item: T, index: number) => number | string;
  render: (item: T, index: number) => ReactNode;
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
          if (selected === identifier(item, index))
            return (
              <Fragment key={identifier(item, index)}>
                {render(item, index)}
              </Fragment>
            );
          else return null;
        })}
      </ul>
    </div>
  );
}
