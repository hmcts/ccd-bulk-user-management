/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package uk.gov.hmcts.ccd.bulk.user.management.utilities;

import uk.gov.hmcts.ccd.bulk.user.management.list.LinkedList;

class JoinUtils {
    public static String join(LinkedList source) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < source.size(); ++i) {
            if (result.length() > 0) {
                result.append(" ");
            }
            result.append(source.get(i));
        }

        return result.toString();
    }
}
