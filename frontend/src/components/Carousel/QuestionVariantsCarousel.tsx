import { useState } from "react";
import { QuestionVariant } from "../../stores/useRoom";

interface Props {
    variants: QuestionVariant[];
};

export default function QuestionVariantsCarousel({ variants }: Props) {
    const [selected, setSelected] = useState(-1);

    return (
        <ul>
            {variants.map(({ uuid, level, options, question }) => {
                return (
                    <li key={uuid}>
                        <p>{level}</p>
                        <h1>{question}</h1>
                        <ul>
                            {options.map((option) => {
                                return (
                                    <li key={uuid + "-" + option}>
                                        {option}
                                    </li>
                                );
                            })}
                        </ul>
                    </li>
                );
            })}
        </ul>
    );
};