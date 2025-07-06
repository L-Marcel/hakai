import { Participant } from "@stores/useRoom";
import styles from "./index.module.scss";
import { FaX } from "react-icons/fa6";
import { kick } from "../../services/room";

interface Props {
  ranked?: boolean;
  userIsRoomOwner?: boolean;
  participants: Participant[];
}

type RankedParticipant = Participant & {
  rank: number;
};

export default function ParticipantsMansoryGrid({
  participants,
  ranked = false,
  userIsRoomOwner = false,
}: Props) {
  return (
    <ul className={styles.masonry}>
      {participants
        .sort((a, b) => a.nickname.localeCompare(b.nickname))
        .sort((a, b) => b.score - a.score)
        .reduce((acc, curr, index) => {
          const last = acc[acc.length - 1];
          const rank =
            index === 0 ? 1 : curr.score === last.score ? last.rank : index + 1;

          acc.push({ ...curr, rank });
          return acc;
        }, [] as RankedParticipant[])
        .map(({ nickname, uuid, score, rank }) => {
          if (ranked) {
            return (
              <li id="ranked" key={uuid}>
                {userIsRoomOwner && (
                  <button onClick={() => kick(uuid)}>
                    <FaX />
                  </button>
                )}
                <h2 role={rank.toString()} className={styles.rank}>
                  {rank}°
                </h2>
                <div>
                  <h3 data-selectable className={styles.nickname}>
                    {nickname}
                  </h3>
                  <p>{score} pontos</p>
                </div>
              </li>
            );
          }

          return (
            <li key={uuid}>
              {userIsRoomOwner && (
                <button onClick={() => kick(uuid)}>
                  <FaX />
                </button>
              )}
              <h3 data-selectable className={styles.nickname}>
                {nickname}
              </h3>
            </li>
          );
        })}
    </ul>
  );
}
